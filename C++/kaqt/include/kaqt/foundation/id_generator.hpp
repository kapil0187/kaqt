#ifndef KAQT_FOUNDATION_ID_GENERATOR_HPP_
#define KAQT_FOUNDATION_ID_GENERATOR_HPP_

#include <iostream>
#include <string>
#include <sstream>

#include <kaqt/foundation/singleton.hpp>

namespace kaqt
{
    namespace foundation
    {
        class IdGenerator : public Singleton < IdGenerator >
        {
            static int64_t id_;
        public:
            std::string new_id();
        };
    }
}

#endif //KAQT_FOUNDATION_ID_GENERATOR_HPP_