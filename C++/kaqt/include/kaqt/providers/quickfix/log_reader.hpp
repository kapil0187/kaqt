#ifndef KAQT_PROVIDERS_QUICKFIX_LOG_READER_HPP_
#define KAQT_PROVIDERS_QUICKFIX_LOG_READER_HPP_

#include <iostream>
#include <string>
#include <memory>
#include <fstream>
#include <sstream>

#include <quickfix/Message.h>

#include <boost/filesystem.hpp>
#include <boost/regex.hpp>

namespace kaqt
{
	namespace providers
	{
		namespace quickfix
		{
			class LogReader
			{
			private:
				std::string input_filename_;
				std::string message_type_;
				std::shared_ptr<std::vector<FIX::Message>> messages_;
			public:
				LogReader(std::string input_file, std::string msgtype = "8");
				~LogReader();
				void process(bool remove_timestamps = true);
				std::shared_ptr<std::vector<FIX::Message>> messages() const;
			};
		}
	}
}

#endif