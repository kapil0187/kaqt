#include <kaqt/foundation/id_generator.hpp>

namespace kaqt
{
    namespace foundation
    {
        int64_t IdGenerator::id_ = 0;

        std::string IdGenerator::new_id()
        {
            ++id_;
            std::stringstream stream;
            stream << id_;
            return stream.str();
        }
    }
}
