#ifndef KAQT_FOUNDATION_SINGLETON_HPP_
#define KAQT_FOUNDATION_SINGLETON_HPP_

#include <iostream>
#include <memory>
#include <mutex>

namespace kaqt
{
    namespace foundation
    {
        template <typename T>
        class Singleton
        {
        private:
            static std::unique_ptr<T> instance_;
            static std::once_flag once_;
            Singleton(const Singleton& source) = delete;
            Singleton& operator=(const Singleton& source) = delete;
            Singleton(Singleton&& source) = delete;
            Singleton& operator=(Singleton&& source) = delete;
        protected:
            Singleton() {}
        public:
            virtual ~Singleton() {}
            static T& instance()
            {
                std::call_once 
                    ( Singleton::once_, 
                    [] () { Singleton::instance_.reset(new T()); }
                );
                return *instance_;
            }
        };

        template<typename T>
        std::unique_ptr<T> Singleton<T>::instance_;

        template<typename T>
        std::once_flag Singleton<T>::once_;
    }
}

#endif // KAQT_FOUNDATION_SINGLETON_HPP_