#include <iostream>

#include <boost/program_options.hpp>
#include <boost/program_options/options_description.hpp>
#include <boost/program_options/variables_map.hpp>

#include <kaqt/providers/tt/symbology_parser.hpp>

namespace po = boost::program_options;
namespace tt = kaqt::providers::tt;
namespace fd = kaqt::foundation;

int main(int argc, char ** argv)
{
	std::string log_file;

	po::options_description generic_opts("Below are the general options.");
	generic_opts.add_options()
		("help,h", "produce help message");

	po::options_description config_options("Below are the general options.");
	config_options.add_options()
		("log_file,l", po::value<std::string>(), "quickfix log file");

	po::options_description cmdline_options;
	cmdline_options.add(generic_opts).add(config_options);

	po::variables_map symbol_parser_vm;
	try
	{
		po::store(po::parse_command_line(argc, argv, cmdline_options), symbol_parser_vm);
	}
	catch (const std::exception& ex)
	{
		std::cout << ex.what() << std::endl;
		std::cout << cmdline_options << std::endl;
		return 1;
	}
	po::notify(symbol_parser_vm);

	if (symbol_parser_vm.count("help") || argc == 1)
	{
		std::cout << cmdline_options << std::endl;
		return 1;
	}


	if (symbol_parser_vm.count("log_file"))
	{
		log_file = symbol_parser_vm["log_file"].as<std::string>();
	}
	else
	{
		std::cout << "log file is a required command line argument" << std::endl;
		return 1;
	}

	kaqt::providers::tt::SymbologyParser parser(log_file);
	parser.process();
	return 0;
}