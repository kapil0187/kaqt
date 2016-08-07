#ifndef KAQT_SYMBOLOGY_SYMBOL_FACTORY_HPP_
#define KAQT_SYMBOLOGY_SYMBOL_FACTORY_HPP_

#include <iostream>
#include <string>
#include <vector>
#include <memory>

#include <zmq.hpp>

#include <kaqt/providers/protobuf/symbology.pb.h>

namespace kaqt
{
    namespace symbology
    {
        class SymbolFactory
        {
        private:
            SymbolFactory(const SymbolFactory& source) = delete;
            SymbolFactory(SymbolFactory&& source) = delete;
            SymbolFactory& operator=(const SymbolFactory& source) = delete;
            SymbolFactory& operator=(SymbolFactory&& source) = delete;
        public:
            SymbolFactory();
            ~SymbolFactory();
            std::vector<kaqt::providers::protobuf::FuturesInstrument>
                instruments_dump(zmq::socket_t& socket);
            std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
                build_request();
            std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
				build_request_id(const int id);
            std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
                build_request_altid(const std::string& altid_type);
            std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
                build_request_exgroup(const std::string& exchange_group);
            std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
                build_request_exchange(const std::string& exchange);
            std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
                build_request_underlying(const std::string& underlying);
        };
    }
}



#endif