#include <kaqt/symbology/client.hpp>

namespace kaqt
{
    namespace symbology
    {
        FuturesInstrumentClient::FuturesInstrumentClient() :
                ctx_(1), client_socket_(ctx_, ZMQ_DEALER)
        {
        }

        FuturesInstrumentClient::~FuturesInstrumentClient()
        {
        }

        void FuturesInstrumentClient::start(const std::string& server_endpoint)
        {
            //client_thread_ = std::thread(&FuturesInstrumentClient::run_server, this);
            server_endpoint_ = server_endpoint;
			this->run_server();
        }

        void FuturesInstrumentClient::run_server()
        {
            char identity[10] = "123";
            client_socket_.setsockopt(ZMQ_IDENTITY, identity, strlen(identity));
            client_socket_.connect(server_endpoint_.c_str());

            //zmq::pollitem_t items[] = { { client_socket_, 0, ZMQ_POLLIN, 0 } };

            try
            {
				//zmq::poll(items, 1, 10);

                auto fi_request = symbol_factory_.build_request();
                std::string fi_request_serialized;
                fi_request->SerializeToString(&fi_request_serialized);

                zmq::message_t request(fi_request_serialized.size());
                memcpy((void *)request.data(), fi_request_serialized.c_str(),
                    fi_request_serialized.size());
                client_socket_.send(request);

				instruments_ = symbol_factory_.instruments_dump(client_socket_);
				if (instruments_.size() > 0)
				{
					std::cout << "Found " << instruments_.size() << " instruments" << std::endl;
				}
            }
            catch (const std::exception& ex)
            {
                std::cout << ex.what() << std::endl;
            }
        }

        void FuturesInstrumentClient::stop()
        {
            client_socket_.close();
            ctx_.close();
        }

		std::vector<kaqt::providers::protobuf::FuturesInstrument> FuturesInstrumentClient::instruments()
		{
			return instruments_;
		}

    }
}
