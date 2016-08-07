#include <kaqt/providers/tt/symbology_parser.hpp>

namespace kaqt
{
	namespace providers
	{
		namespace tt
		{
			SymbologyParser::SymbologyParser(const std::string& filename) : log_reader_(filename, "d"),
				writer_(kaqt::providers::tt::Constants::SYMBOLOGY_FILENAME)
			{
			}

			SymbologyParser::~SymbologyParser()
			{
			}

			std::map<int, std::string> SymbologyParser::PARSER_COLUMNS =
			{
				{ FIX::FIELD::SecurityID, "SecurityID" },
				{ FIX::FIELD::Symbol, "Symbol"},
				{ FIX::FIELD::SecurityDesc, "SecurityDesc" },
				{ FIX::FIELD::MaturityMonthYear, "MaturityMonthYear" },
				{ FIX::FIELD::SecurityExchange, "SecurityExchange" },
				{ FIX::FIELD::EventType, "EventType" },
				{ FIX::FIELD::EventDate, "EventDate" },
				{ kaqt::providers::tt::Constants::CONTRACT_SYMBOL_FIELD, "ContractSymbol" },
				{ kaqt::providers::tt::Constants::PRICE_DISPLAY_TYPE_FIELD, "PriceDisplayType"},
				{ kaqt::providers::tt::Constants::TICK_SIZE_FIELD, "TickSize"},
				{ kaqt::providers::tt::Constants::POINT_VALUE_FIELD, "PointValue"},
				{ kaqt::providers::tt::Constants::EXCHANGE_TICK_SIZE, "ExchangeTickSize"},
				{ kaqt::providers::tt::Constants::EXCHANGE_POINT_VALUE , "ExchangePointValue"}
			};

			std::string SymbologyParser::column_name(int fix_tag)
			{
				auto iter = PARSER_COLUMNS.find(fix_tag);
				std::string rv = "";
				if (iter != PARSER_COLUMNS.end())
				{
					rv = iter->second;
				}
				return rv;
			}

			void SymbologyParser::process()
			{
				writer_.open();
				int i = 1;
				for (auto & col : PARSER_COLUMNS)
				{
					writer_.add_column(i, col.second);
					++i;
				}

				log_reader_.process();
				std::vector<FIX::Message> messages = *log_reader_.messages();

				std::cout << "number of messages: " << messages.size() << std::endl;

				std::vector<std::string> to_write;

				for (auto & m : messages)
				{
					for (auto & col : PARSER_COLUMNS)
					{
						if (m.isSetField(col.first))
						{
							if (col.first == FIX::FIELD::SecurityDesc)
							{
								to_write.push_back("\"" + m.getField(col.first) + "\"");
							}
							else
							{
								to_write.push_back(m.getField(col.first));
							}
						}
						else
						{
							to_write.push_back("");
						}
					}
					writer_.insert_row(to_write);
					to_write.clear();
				}

				writer_.write(true);
				
				writer_.close();
			}
		}
	}
}