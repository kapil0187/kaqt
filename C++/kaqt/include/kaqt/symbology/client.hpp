#ifndef KAQT_SYMBOLOGY_CLIENT_HPP_
#define KAQT_SYMBOLOGY_CLIENT_HPP_

#ifdef _MSC_VER
#pragma warning (disable : 4996)
#endif

#include <iostream>
#include <string>
#include <thread>
#include <memory>
#include <vector>
#include <functional>

#include <zmq.hpp>

#include <kaqt/providers/protobuf/symbology.pb.h>
#include <kaqt/providers/zeromq/zhelpers.hpp>
#include <kaqt/symbology/symbol_factory.hpp>
#include <kaqt/symbology/symbol_factory.hpp>

namespace kaqt
{
    namespace symbology
    {
        class FuturesInstrumentClient
        {
        private:
            std::thread client_thread_;
            zmq::context_t ctx_;
            zmq::socket_t client_socket_;
            std::string server_endpoint_;
            SymbolFactory symbol_factory_;
			std::vector<kaqt::providers::protobuf::FuturesInstrument> instruments_;
            FuturesInstrumentClient(const FuturesInstrumentClient& source) = delete;
            FuturesInstrumentClient& operator=(const FuturesInstrumentClient& source) = delete;
            FuturesInstrumentClient(FuturesInstrumentClient&& source) = delete;
            FuturesInstrumentClient& operator=(FuturesInstrumentClient&& source) = delete;
            void run_server();
        public:
            FuturesInstrumentClient();
            ~FuturesInstrumentClient();
			std::vector<kaqt::providers::protobuf::FuturesInstrument> instruments();
            void start(const std::string& server_endpoint);
            void stop();
        };

    }
}


#endif // KAQT_SYMBOLOGY_CLIENT_HPP_