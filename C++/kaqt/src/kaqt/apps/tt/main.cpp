#include <iostream>

#include <boost/program_options.hpp>
#include <boost/program_options/options_description.hpp>
#include <boost/program_options/variables_map.hpp>

#include <kaqt/foundation/id_generator.hpp>
#include <kaqt/providers/tt/constants.hpp>
#include <kaqt/providers/tt/fixadaptor_client.hpp>

namespace po = boost::program_options;
namespace tt = kaqt::providers::tt;
namespace fd = kaqt::foundation;

int main(int argc, char ** argv)
{
    std::string tt_fix_config;
    std::string tt_fix_password;
    bool security_def_mode = true;

    po::options_description generic_opts("Below are the general options.");
    generic_opts.add_options()
        ("help,h", "produce help message");

    po::options_description config_options("Below are the general options.");
    config_options.add_options()
        ("config,c", po::value<std::string>(), "quickfix config file");
    config_options.add_options()
        ("password,p", po::value<std::string>(), "password for connecting to TT fix adaptor");
    config_options.add_options()
        ("securitydef,s", po::bool_switch()->default_value(false), "security definition mode");

    po::options_description cmdline_options;
    cmdline_options.add(generic_opts).add(config_options);

    po::variables_map tt_app_vm;
    try
    {
        po::store(po::parse_command_line(argc, argv, cmdline_options), tt_app_vm);
    }
    catch (const std::exception& ex)
    {
        std::cout << ex.what() << std::endl;
        std::cout << cmdline_options << std::endl;
        return 1;
    }
    po::notify(tt_app_vm);

    if (tt_app_vm.count("help") || argc == 1)
    {
        std::cout << cmdline_options << std::endl;
        return 1;
    }


    if (tt_app_vm.count("config"))
    {
        tt_fix_config = tt_app_vm["config"].as<std::string>();
    }
    else
    {
        std::cout << "Config file not found" << std::endl;
        return 1;
    }

    if (tt_app_vm.count("password"))
    {
        tt_fix_password = tt_app_vm["password"].as<std::string>();
    }
    else
    {
        std::cout << "Password is a required field" << std::endl;
        return 1;
    }

    if (tt_app_vm.count("securitydef"))
    {
        security_def_mode = tt_app_vm["securitydef"].as<bool>();
    }

    if (tt_fix_config.empty())
    {
        std::cerr << "Empty config file " << tt_fix_config << std::endl;
        return 1;
    }

    tt::FixAdaptorClient client(tt_fix_config, tt_fix_password, 
        tt::Constants::PASSWORD_FIELD, security_def_mode);
    client.start();

    char quit_signal;

    while (true)
    {
        std::cin >> quit_signal;
        if (quit_signal == 'q')
        {
            break;
        }
    }

    client.stop();

    return 0;
}