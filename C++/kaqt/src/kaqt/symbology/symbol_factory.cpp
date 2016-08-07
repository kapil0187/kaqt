#include <kaqt/symbology/symbol_factory.hpp>

namespace kaqt
{
    namespace symbology
    {
        SymbolFactory::SymbolFactory()
        {
        }

        SymbolFactory::~SymbolFactory()
        {
        }
        
        std::vector<kaqt::providers::protobuf::FuturesInstrument>
			SymbolFactory::instruments_dump(zmq::socket_t& socket)
        {
            std::vector<kaqt::providers::protobuf::FuturesInstrument> rv;

            while (true)
            {
                zmq::message_t message;
                socket.recv(&message);

				std::cout << "Received Message has size = " << message.size() << std::endl;

                kaqt::providers::protobuf::FuturesInstrumentResponse response;
                response.ParseFromArray(message.data(), message.size());

                auto all_instr = response.instruments();

                for (auto & inst : all_instr)
                {
                    rv.push_back(inst);
                }

                int more = 0;           
                size_t more_size = sizeof(more);
                socket.getsockopt(ZMQ_RCVMORE, &more, &more_size);
                if (!more)
                    break;                
            }

            return rv;
        }

        std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
            SymbolFactory::build_request()
        {
			std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest> rv(
				new kaqt::providers::protobuf::FuturesInstrumentRequest);
			return rv;
        }

        std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
            SymbolFactory::build_request_id(const int id)
        {
			std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest> rv(
				new kaqt::providers::protobuf::FuturesInstrumentRequest);
			rv->set_id(id);
			return rv;
        }

        std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
            SymbolFactory::build_request_altid(const std::string& altid_type)
        {
            return nullptr;
        }
        
        std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
            SymbolFactory::build_request_exgroup(const std::string& exchange_group)
        {
            return nullptr;
        }
        
        std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
            SymbolFactory::build_request_exchange(const std::string& exchange)
        {
            return nullptr;
        }
        
        std::unique_ptr<kaqt::providers::protobuf::FuturesInstrumentRequest>
            SymbolFactory::build_request_underlying(const std::string& underlying)
        {
            return nullptr;
        }
    }
}
