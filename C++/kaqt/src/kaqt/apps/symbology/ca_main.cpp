#include <iostream>

#include <boost/program_options.hpp>
#include <boost/program_options/options_description.hpp>
#include <boost/program_options/variables_map.hpp>

#include <kaqt/symbology/symbol_repository.hpp>

namespace po = boost::program_options;

void menu()
{
	std::cout << "Choose among the options below" << std::endl;
	std::cout << "q: Quit\ni: Query all instruments" << std::endl;
}

int main(int argc, char ** argv)
{
	std::string connection_string;

	po::options_description generic_opts("Below are the general options.");
	generic_opts.add_options()
		("help,h", "produce help message");

	po::options_description config_options("Below are the general options.");
	config_options.add_options()
		("connection,c", po::value<std::string>(), "quickfix config file");

	po::options_description cmdline_options;
	cmdline_options.add(generic_opts).add(config_options);

	po::variables_map app_vm;
	try
	{
		po::store(po::parse_command_line(argc, argv, cmdline_options), app_vm);
	}
	catch (const std::exception& ex)
	{
		std::cout << ex.what() << std::endl;
		std::cout << cmdline_options << std::endl;
		return 1;
	}
	po::notify(app_vm);

	if (app_vm.count("help") || argc == 1)
	{
		std::cout << cmdline_options << std::endl;
		return 1;
	}


	if (app_vm.count("connection"))
	{
		connection_string = app_vm["connection"].as<std::string>();
	}
	else
	{
		std::cout << "Connection string is missing" << std::endl;
		return 1;
	}

	kaqt::symbology::SymbolRepository::instance().initialize(connection_string);

	char user_input;
	while (true)
	{
		menu();
		std::cin >> user_input;
		if (user_input == 'q')
		{
			break;
		}
		else if (user_input == 'i')
		{
			auto inst = kaqt::symbology::SymbolRepository::instance().query_all();
			std::cout << "Received " << inst.size() << " instruments" << std::endl;
		}
		else
		{
			menu();
		}
	}

	kaqt::symbology::SymbolRepository::instance().close();
}