#ifndef KAQT_PROVIDERS_TT_SYMBOLOGY_PARSE_HPP_
#define KAQT_PROVIDERS_TT_SYMBOLOGY_PARSE_HPP_

#include <iostream>
#include <string>
#include <map>

#include <kaqt/foundation/csv_writer.hpp>
#include <kaqt/providers/tt/constants.hpp>
#include <kaqt/providers/quickfix/log_reader.hpp>

#include <quickfix/Field.h>

namespace kaqt
{
	namespace providers
	{
		namespace tt
		{
			class SymbologyParser
			{
			private:
				kaqt::foundation::CsvWriter writer_;
				kaqt::providers::quickfix::LogReader log_reader_;
				static std::map<int, std::string> PARSER_COLUMNS;
			public:
				SymbologyParser(const std::string& filename);
				~SymbologyParser();
				static std::string column_name(int fix_tag);
				void process();
			};
		}
	}
}


#endif // KAQT_PROVIDERS_TT_SYMBOLOGY_PARSE_HPP_