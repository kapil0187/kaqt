#ifndef KAQT_FOUNDATION_FACTORY_HPP_
#define KAQT_FOUNDATION_FACTORY_HPP_

#include <iostream>
#include <memory>
#include <functional>

template < typename TKey, typename TType >
class Factory
{
private:
    typedef std::function<std::unique_ptr<TType>(void)> create_function;
    typedef std::map<TKey, create_function> functions_map;

    functions_map factory_funcs_;

    Factory(const Factory& source) = delete;
    Factory& operator=(const Factory& source) = delete;
    Factory(Factory&& source) = delete;
    Factory& operator=(Factory&& source) = delete;
public:
    Factory() {}
    ~Factory() {}
    void register_type(const TKey& key, create_function cf)
    {
        factory_funcs_[key] = cf;
    }

    std::unique_ptr<TType> create(const TKey& key)
    {
        typename functions_map::iterator it = factory_funcs_.find(key);
        if (it != factory_funcs_.end())
        {
            return it->second();
        }
        else
        {
            return nullptr;
        }
    }
};

#endif