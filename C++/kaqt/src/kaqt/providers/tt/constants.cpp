#include <kaqt/providers/tt/constants.hpp>

namespace kaqt
{
    namespace providers
    {
        namespace tt
        {
            const int Constants::CONTRACT_SYMBOL_FIELD = 10455;
            const int Constants::PRICE_DISPLAY_TYPE_FIELD = 16451;
            const int Constants::TICK_SIZE_FIELD = 16452;
            const int Constants::POINT_VALUE_FIELD = 16454;
            const int Constants::EXCHANGE_TICK_SIZE = 16552;
            const int Constants::EXCHANGE_POINT_VALUE = 16554;

            const std::string Constants::PRICE_SESSION_ID = "FIX.4.2:KAPILDTS->TTDEV13P";
            const std::string Constants::ORDEREX_SESSION_ID = "FIX.4.2:KAPILDTS->TTDEV13O";
            const std::vector<std::string> Constants::SUBSCRIBED_SYMBOLS =
            {
                ""
            };

			const std::string Constants::SYMBOLOGY_FILENAME = "TTSymbols.csv";
        }
    }
}
