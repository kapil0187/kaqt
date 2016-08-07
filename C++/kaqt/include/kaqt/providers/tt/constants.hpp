#ifndef KAQT_MARKETDATA_TT_CONSTANTS_HPP_
#define KAQT_MARKETDATA_TT_CONSTANTS_HPP_

#include <iostream>
#include <string>
#include <vector>

#include <quickfix/Field.h>

namespace kaqt
{
    namespace providers
    {
        namespace tt
        {
            class Constants
            {
            public:
                static const int PASSWORD_FIELD = 96;
				static const int REQUEST_TICK_TABLE_FIELD = 17000;
				static const int CONTRACT_SYMBOL_FIELD;
				static const int PRICE_DISPLAY_TYPE_FIELD;
				static const int TICK_SIZE_FIELD;
				static const int POINT_VALUE_FIELD;
				static const int EXCHANGE_TICK_SIZE;
				static const int EXCHANGE_POINT_VALUE;

                static const std::string PRICE_SESSION_ID;
                static const std::string ORDEREX_SESSION_ID;

                static const std::vector<std::string> SUBSCRIBED_SYMBOLS;

				static const std::string SYMBOLOGY_FILENAME;
            };
        }
    }
}

#endif // KAQT_MARKETDATA_TT_CONSTANTS_HPP_