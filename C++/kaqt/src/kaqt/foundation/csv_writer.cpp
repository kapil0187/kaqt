#include <kaqt/foundation/csv_writer.hpp>

namespace
{
    std::string vector_to_string(const std::vector<std::string> v)
    {
        std::string line = "\0";
        for (auto & val : v)
        {
            if (line != "\0")
            {
                line = line + "," + val;
            }
            else
            {
                line = val;
            }
        }
        line = line + "\n";
        return line;
    }

    std::string map_to_string(const std::map<int, std::string> m)
    {
        std::string line = "\0";
        for (auto & val : m)
        {
            if (line != "\0")
            {
                line = line + "," + val.second;
            }
            else
            {
                line = val.second;
            }
        }
        line = line + "\n";
        return line;
    }
}
namespace kaqt
{
    namespace foundation
    {
        CsvWriter::CsvWriter(const std::string& filename) : filename_(filename)
        {
        }

		CsvWriter::~CsvWriter()
		{
		}

        void CsvWriter::open()
        {
            file_stream_ = std::unique_ptr<std::ofstream>(new std::ofstream());
            file_stream_->open(filename_);
        }

        void CsvWriter::write(bool include_colnames)
        {
            if (include_colnames)
            {
                auto to_write = map_to_string(column_names_);
                *file_stream_ << (to_write);
            }

            for (auto & r : all_rows_)
            {
				*file_stream_ << (r.c_str());
            }
        }

        void CsvWriter::close()
        {
            file_stream_->close();
        }

        void CsvWriter::add_column(int index, std::string& column_name)
        {
            column_names_.insert(std::pair<int, std::string>(index, column_name));
        }

        void CsvWriter::insert_row(const std::vector<std::string> row)
        {
            if (column_names_.size() > 0)
            {
                if (row.size() == column_names_.size())
                {
                    auto to_write = vector_to_string(row);
                    all_rows_.push_back(to_write);
                }
            }
        }
    }
}

