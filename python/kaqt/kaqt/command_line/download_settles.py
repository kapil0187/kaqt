#!/usr/bin/env python3

from optparse import OptionParser
import logging
import os
import numpy as np
import datetime
import pysftp
import re

from kaqt.foundation.constants import tickdata_server_ip
from kaqt.foundation.constants import settles_data_path
from kaqt.foundation.constants import settles_remote_dir
from kaqt.foundation.io import kaqt_path

def main():
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    print("*************** " + str(datetime.datetime.now()) + " ***************")

    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option("-q", "--quiet", help="Don't show Verbose output",
                      action='store_false', default=True, dest='verbose')
    parser.add_option("-u", "--username", help="Username to connect to sftp",
                      action="store", type="string", dest="username")
    parser.add_option("-p", "--password", help="Password to connect to sftp",
                      action="store", type="string", dest="password")
    parser.add_option("-d", "--dir", help="Directory to save data",
                      action="store", type="string", dest="dir")

    (options, args) = parser.parse_args()
    missing_args = not all((options.username, options.password))

    if missing_args:
        if options.verbose is True:
            logger.error("Missing required arguments")
        parser.print_help()
    else:
        server_address = tickdata_server_ip
        if kaqt_path() == '':
            logger.error("KAQT_PATH environment variable is not set")
            return
        save_dir = options.dir if options.dir else os.path.join(kaqt_path(), settles_data_path)
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)
        if options.verbose is True:
            logger.info("Connecting to server ip " + server_address)
            logger.info("Saving to " + save_dir)

        connection = None
        try:
            connection = pysftp.Connection(server_address,
                                           username=options.username,
                                           password=options.password)
        except (pysftp.ConnectionException,
                pysftp.CredentialException,
                pysftp.SSHException,
                pysftp.AuthenticationException) as e:
            logger.error("Sftp connection error. Error message: ", e)
            connection.close()
            return

        existing_files = np.array(os.listdir(save_dir))
        r = re.compile("\\S*-Futures_Settles\\S*[0-9][.]csv[.]gz")
        with connection.cd():
            if connection.exists(settles_remote_dir):
                connection.chdir(settles_remote_dir)
                server_files = np.array(connection.listdir())
                server_files = [f for f in server_files if r.match(f)]
                to_download = np.setdiff1d(server_files, existing_files)
                if len(to_download) > 0:
                    for filename in to_download:
                        to_move = os.path.abspath(filename)
                        dest_file = os.path.join(save_dir, filename)
                        connection.get(filename)
                        downloaded_file = os.path.join('.', to_move)
                        if os.path.isfile(downloaded_file):
                            if options.verbose:
                                logger.info("Moving " + str(downloaded_file) + " to " + str(dest_file))
                            os.rename(downloaded_file, dest_file)
                else:
                    logger.info("No new files to download")
        if connection is not None:
            connection.close()

if __name__ == '__main__':
    main()








