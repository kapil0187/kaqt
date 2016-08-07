#include <kaqt/providers/quickfix/fix42/message_factory.hpp>

namespace kaqt
{
    namespace providers
    {
        namespace quickfix
        {
            namespace fix42
            {
                MessageFactory::MessageFactory()
                {
                }

                MessageFactory::~MessageFactory()
                {
                }

                std::unique_ptr<FIX42::SecurityDefinitionRequest> MessageFactory::build_secdef_request()
                {
                    auto id = kaqt::foundation::IdGenerator::instance().new_id();
                    std::unique_ptr<FIX42::SecurityDefinitionRequest> sec_def_request(new FIX42::SecurityDefinitionRequest);
                    sec_def_request->setField(FIX::FIELD::SecurityReqID, id);
                    return sec_def_request;
                }

                std::unique_ptr<FIX42::SecurityDefinitionRequest> MessageFactory::build_secdef_request_ex(
                    const std::string& exchange)
                {
                    auto sec_def_request = this->build_secdef_request();
                    sec_def_request->setField(FIX::FIELD::SecurityExchange, exchange);
                    return sec_def_request;
                }

                std::unique_ptr<FIX42::SecurityDefinitionRequest> MessageFactory::build_secdef_request_pdt(
                    const std::string& product)
                {
                    auto sec_def_request = this->build_secdef_request();
                    sec_def_request->setField(FIX::FIELD::SecurityType, product);
                    return sec_def_request;
                }

                std::unique_ptr<FIX42::SecurityDefinitionRequest> MessageFactory::build_secdef_request_ex_pdt(
                    const std::string& exchange, const std::string& product)
                {
                    auto sec_def_request = this->build_secdef_request_ex(exchange);
                    sec_def_request->setField(FIX::FIELD::SecurityType, product);
                    return sec_def_request;
                }

                std::unique_ptr<FIX42::SecurityDefinitionRequest> MessageFactory::build_secdef_request_ex_pdt_sym(
                    const std::string& exchange, const std::string& product, const std::string& symbol)
                {
                    auto sec_def_request = this->build_secdef_request_ex_pdt(exchange, product);
                    sec_def_request->setField(FIX::FIELD::Symbol, symbol);
                    return sec_def_request;
                }


                std::unique_ptr<FIX42::SecurityDefinitionRequest> MessageFactory::build_secdef_request_secid(
                    const std::string& security_id)
                {
                    auto sec_def_request = this->build_secdef_request();
                    sec_def_request->setField(FIX::FIELD::SecurityID, security_id);
                    return sec_def_request;
                }

                std::unique_ptr<FIX42::SecurityStatusRequest> MessageFactory::build_secstatus_request(
                    const FIX::SubscriptionRequestType& type)
                {
                    auto id = kaqt::foundation::IdGenerator::instance().new_id();
                    std::unique_ptr<FIX42::SecurityStatusRequest> secstatus_request(new FIX42::SecurityStatusRequest);
                    secstatus_request->setField(FIX::FIELD::SecurityStatusReqID, id);
                    secstatus_request->setField(type);
                    return secstatus_request;
                }

                std::unique_ptr<FIX42::MarketDataRequest> MessageFactory::build_mktdata_bbo_subscribe(
                    const std::string& security_id)
                {
                    auto id = kaqt::foundation::IdGenerator::instance().new_id();
					subscribed_securities_.insert(std::pair<std::string, std::string>(security_id, id));
                    std::unique_ptr<FIX42::MarketDataRequest> mktdata_req(new FIX42::MarketDataRequest);
                    mktdata_req->setField(FIX::FIELD::MDReqID, id);
                    mktdata_req->set(FIX::SubscriptionRequestType(FIX::SubscriptionRequestType_SNAPSHOT_PLUS_UPDATES));
                    mktdata_req->set(FIX::MarketDepth(1));
                    mktdata_req->set(FIX::MDUpdateType(FIX::MDUpdateType_FULL_REFRESH));
                    mktdata_req->set(FIX::AggregatedBook(FIX::AggregatedBook_YES));

                    FIX42::MarketDataRequest::NoMDEntryTypes no_md_bid;
                    no_md_bid.set(FIX::MDEntryType(FIX::MDEntryType_BID));
                    mktdata_req->addGroup(no_md_bid);

                    FIX42::MarketDataRequest::NoMDEntryTypes no_md_ask;
                    no_md_ask.set(FIX::MDEntryType(FIX::MDEntryType_OFFER));
                    mktdata_req->addGroup(no_md_ask);

                    FIX42::MarketDataRequest::NoMDEntryTypes no_md_trade;
                    no_md_trade.set(FIX::MDEntryType(FIX::MDEntryType_TRADE));
                    mktdata_req->addGroup(no_md_trade);

                    FIX42::MarketDataRequest::NoRelatedSym no_rel_sym;
                    no_rel_sym.set(FIX::Symbol("GE"));
                    no_rel_sym.set(FIX::SecurityID(security_id));
                    no_rel_sym.set(FIX::SecurityExchange("CME"));
                    no_rel_sym.set(FIX::SecurityType(FIX::SecurityType_FUTURE));
                    mktdata_req->addGroup(no_rel_sym);

                    return mktdata_req;
                }

				std::unique_ptr<FIX42::MarketDataRequest> MessageFactory::build_mktdata_bbo_unsubscribe(
					const std::string& security_id)
				{
					std::unique_ptr<FIX42::MarketDataRequest> mktdata_req(new FIX42::MarketDataRequest);
					mktdata_req->setField(FIX::FIELD::MDReqID, subscribed_securities_[security_id]);
					mktdata_req->set(FIX::SubscriptionRequestType(FIX::SubscriptionRequestType_DISABLE_PREVIOUS_SNAPSHOT_PLUS_UPDATE_REQUEST));
					mktdata_req->set(FIX::MarketDepth(1));
					mktdata_req->set(FIX::MDUpdateType(FIX::MDUpdateType_FULL_REFRESH));
					mktdata_req->set(FIX::AggregatedBook(FIX::AggregatedBook_YES));

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_bid;
					no_md_bid.set(FIX::MDEntryType(FIX::MDEntryType_BID));
					mktdata_req->addGroup(no_md_bid);

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_ask;
					no_md_ask.set(FIX::MDEntryType(FIX::MDEntryType_OFFER));
					mktdata_req->addGroup(no_md_ask);

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_trade;
					no_md_trade.set(FIX::MDEntryType(FIX::MDEntryType_TRADE));
					mktdata_req->addGroup(no_md_trade);

                    FIX42::MarketDataRequest::NoRelatedSym no_rel_sym;
                    no_rel_sym.set(FIX::Symbol("GE"));
                    no_rel_sym.set(FIX::SecurityID(security_id));
                    no_rel_sym.set(FIX::SecurityExchange("CME"));
                    no_rel_sym.set(FIX::SecurityType(FIX::SecurityType_FUTURE));

					subscribed_securities_.erase(security_id);

					return mktdata_req;
				}

				std::unique_ptr<FIX42::MarketDataRequest> MessageFactory::build_mktdata_depth_subscribe(
					const std::string& security_id, const int depth)
				{
					auto id = kaqt::foundation::IdGenerator::instance().new_id();
					std::unique_ptr<FIX42::MarketDataRequest> mktdata_req(new FIX42::MarketDataRequest);
					mktdata_req->setField(FIX::FIELD::MDReqID, id);
                    mktdata_req->set(FIX::SubscriptionRequestType(FIX::SubscriptionRequestType_SNAPSHOT_PLUS_UPDATES));
					mktdata_req->set(FIX::MarketDepth(depth));
					mktdata_req->set(FIX::MDUpdateType(FIX::MDUpdateType_FULL_REFRESH));
					mktdata_req->set(FIX::AggregatedBook(FIX::AggregatedBook_YES));

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_bid;
					no_md_bid.set(FIX::MDEntryType(FIX::MDEntryType_BID));
					mktdata_req->addGroup(no_md_bid);

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_ask;
					no_md_ask.set(FIX::MDEntryType(FIX::MDEntryType_OFFER));
					mktdata_req->addGroup(no_md_ask);

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_trade;
					no_md_trade.set(FIX::MDEntryType(FIX::MDEntryType_TRADE));
					mktdata_req->addGroup(no_md_trade);

                    FIX42::MarketDataRequest::NoRelatedSym no_rel_sym;
                    no_rel_sym.set(FIX::Symbol("GE"));
                    no_rel_sym.set(FIX::SecurityID(security_id));
                    no_rel_sym.set(FIX::SecurityExchange("CME"));
                    no_rel_sym.set(FIX::SecurityType(FIX::SecurityType_FUTURE));
                    mktdata_req->addGroup(no_rel_sym);

					return mktdata_req;
				}

				std::unique_ptr<FIX42::MarketDataRequest> MessageFactory::build_mktdata_depth_unsubscribe(
					const std::string& security_id)
				{
					std::unique_ptr<FIX42::MarketDataRequest> mktdata_req(new FIX42::MarketDataRequest);
					mktdata_req->setField(FIX::FIELD::MDReqID, subscribed_securities_[security_id]);
					mktdata_req->set(FIX::SubscriptionRequestType(FIX::SubscriptionRequestType_DISABLE_PREVIOUS_SNAPSHOT_PLUS_UPDATE_REQUEST));
					mktdata_req->set(FIX::MDUpdateType(FIX::MDUpdateType_FULL_REFRESH));
					mktdata_req->set(FIX::AggregatedBook(FIX::AggregatedBook_YES));

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_bid;
					no_md_bid.set(FIX::MDEntryType(FIX::MDEntryType_BID));
					mktdata_req->addGroup(no_md_bid);

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_ask;
					no_md_ask.set(FIX::MDEntryType(FIX::MDEntryType_OFFER));
					mktdata_req->addGroup(no_md_ask);

					FIX42::MarketDataRequest::NoMDEntryTypes no_md_trade;
					no_md_trade.set(FIX::MDEntryType(FIX::MDEntryType_TRADE));
					mktdata_req->addGroup(no_md_trade);

                    FIX42::MarketDataRequest::NoRelatedSym no_rel_sym;
                    no_rel_sym.set(FIX::Symbol("GE"));
                    no_rel_sym.set(FIX::SecurityID(security_id));
                    no_rel_sym.set(FIX::SecurityExchange("CME"));
                    no_rel_sym.set(FIX::SecurityType(FIX::SecurityType_FUTURE));
                    mktdata_req->addGroup(no_rel_sym);

					subscribed_securities_.erase(security_id);

					return mktdata_req;
				}
            }
        }
    }
}