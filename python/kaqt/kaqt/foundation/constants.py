import tables

# Provide the server ip address to download tick data
tickdata_server_ip = '10.10.100.222'

# Provide the directory where tick data is saved on remote server
tickdata_remote_dir = '/home/storage/csv/'

# Provide the directory where settles data is saved on remote server
settles_remote_dir = '/home/storage/archive'

# Provide relative path with respect to the KAQT_PATH env variable
# This directory is where all data is downloaded
raw_data_path = 'data/raw'

# Provide relative path with respect to the KAQT_PATH env variable
# This directory is where all the cleaned data is saved as hdf5
clean_data_path = 'data/clean'

# Provide relative path with respect to the KAQT_PATH env variable
# This directory is where all the settles data is downloaded
settles_data_path = 'data/settles'

# File that contains the historical settles data
historical_settles_filename = 'OldSettles.csv'

# HDF file with all the settles data
settles_hdf_filename = "settles.h5"

# HDF file with all the tick data
quotes_hdf_filename = 'quotes.h5'

# HDF file with all the tick data
trades_hdf_filename = 'trades.h5'

# Default kaqt path directory
kaqt_dir = '/home/ksharma/dev/kaqt'

# Futures Month Codes
futures_month_codes = {
    'January': 'F',
    'February': 'G',
    'March': 'H',
    'April': 'J',
    'May': 'K',
    'June': 'M',
    'July': 'N',
    'August': 'Q',
    'September': 'U',
    'October': 'V',
    'November': 'X',
    'December': 'Z'
}

# FIlters used in H5 files
hdf_repos_filters = tables.Filters(complevel=1, complib='zlib')