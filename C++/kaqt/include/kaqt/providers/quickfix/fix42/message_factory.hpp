#ifndef KAQT_PROVIDERS_QUICKFIX_FIX42_MESSAGE_FACTORY_HPP_
#define KAQT_PROVIDERS_QUICKFIX_FIX42_MESSAGE_FACTORY_HPP_

#include <iostream>
#include <string>
#include <map>

#include <kaqt/foundation/id_generator.hpp>

#include <quickfix/fix42/SecurityDefinition.h>
#include <quickfix/fix42/SecurityDefinitionRequest.h>
#include <quickfix/fix42/SecurityStatus.h>
#include <quickfix/fix42/SecurityStatusRequest.h>
#include <quickfix/fix42/MarketDataRequest.h>

namespace kaqt
{
    namespace providers
    {
        namespace quickfix
        {
            namespace fix42
            {
                class MessageFactory
                {
                private:
					std::map<std::string /*Security ID*/, std::string /*Request ID*/> subscribed_securities_;
                public:
                    MessageFactory();
                    ~MessageFactory();
                    MessageFactory(const MessageFactory& source) = delete;
                    MessageFactory(MessageFactory&& source) = delete;
                    MessageFactory& operator=(const MessageFactory& source) = delete;
                    MessageFactory& operator=(MessageFactory&& source) = delete;
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> build_secdef_request();
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> build_secdef_request_ex(
                        const std::string& exchange);
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> build_secdef_request_pdt(
                        const std::string& product);
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> build_secdef_request_ex_pdt(
                        const std::string& exchange, const std::string& product);
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> build_secdef_request_ex_pdt_sym(
                        const std::string& exchange, const std::string& product, const std::string& symbol);
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> build_secdef_request_secid(
                        const std::string& security_id);
                    std::unique_ptr<FIX42::SecurityStatusRequest> build_secstatus_request(
                        const FIX::SubscriptionRequestType& type);
                    std::unique_ptr<FIX42::MarketDataRequest> build_mktdata_bbo_subscribe(const std::string& security_id);
					std::unique_ptr<FIX42::MarketDataRequest> build_mktdata_bbo_unsubscribe(const std::string& security_id);
					std::unique_ptr<FIX42::MarketDataRequest> build_mktdata_depth_subscribe(const std::string& security_id, const int depth);
					std::unique_ptr<FIX42::MarketDataRequest> build_mktdata_depth_unsubscribe(const std::string& security_id);
                };
            }
        }
    }
}


#endif