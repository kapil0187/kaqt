#ifndef KAQT_FOUNDATION_CSV_WRITER_HPP
#define KAQT_FOUNDATION_CSV_WRITER_HPP

#include <iostream>
#include <string>
#include <fstream>
#include <map>
#include <vector>
#include <memory>

namespace kaqt
{
    namespace foundation
    {
        class CsvWriter
        {
        private:
            std::string filename_;
            std::unique_ptr<std::ofstream> file_stream_;
            std::map<int, std::string> column_names_;
            std::vector<std::string> all_rows_;
            CsvWriter(const CsvWriter& source) = delete;
            CsvWriter(CsvWriter&& source) = delete;
            CsvWriter& operator=(const CsvWriter& source) = delete;
        public:
            CsvWriter(const std::string& filename);
			~CsvWriter();
            void open();
            void write(bool include_colnames = true);
            void close();
            void add_column(int index, std::string& column_name);
            void insert_row(const std::vector<std::string> row);
        };
    }
}

#endif // KAQT_FOUNDATION_CSV_WRITER_HPP
