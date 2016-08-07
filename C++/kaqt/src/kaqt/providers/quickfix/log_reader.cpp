#include <kaqt/providers/quickfix/log_reader.hpp>

namespace kaqt
{
	namespace providers
	{
		namespace quickfix
		{
			LogReader::LogReader(std::string input_file, std::string msgtype) :
				input_filename_(input_file), message_type_(msgtype), messages_(new std::vector<FIX::Message>)
			{
			}

			LogReader::~LogReader()
			{
			}

			void LogReader::process(bool remove_timestamps)
			{
				if (boost::filesystem::exists(input_filename_))
				{
					std::ifstream log_file_stream(input_filename_);
					std::string line;

					while (std::getline(log_file_stream, line))
					{
						std::istringstream iss(line);
						auto m = iss.str();

						if (remove_timestamps)
						{
							boost::regex rgx("\\S+ : 8");
							m = boost::regex_replace(m, rgx, "8");
						}

						FIX::Message msg;
						try
						{
							msg = FIX::Message(m);
						}
						catch (const std::exception& ex)
						{
							std::cout << ex.what() << std::endl;
						}
						
						if (msg.getHeader().getField(35) == message_type_)
						{
							messages_->push_back(msg);
						}
					}

					if (log_file_stream.is_open())
					{
						log_file_stream.close();
					}
				}
			}

			std::shared_ptr<std::vector<FIX::Message>> LogReader::messages() const
			{
				return messages_;
			}
		}
	}
}