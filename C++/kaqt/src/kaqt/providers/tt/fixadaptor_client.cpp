#include <kaqt/providers/tt/fixadaptor_client.hpp>
#include <kaqt/providers/tt/constants.hpp>

namespace kaqt
{
    namespace providers
    {
        namespace tt
        {
            FixAdaptorClient::FixAdaptorClient(const std::string& config_filename, 
                const std::string& password, const int password_field,
                const bool security_def_mode = false) :
                config_filename_(config_filename), password_(password),
                require_password_(true), password_field_(password_field), security_def_mode_(security_def_mode)
            {
                connected_ = false;
            }

            FixAdaptorClient::~FixAdaptorClient()
            {
                if (connected_)
                {
                    this->stop();
                }
            }

            void FixAdaptorClient::onCreate(const FIX::SessionID& session_id)
            {
                std::cout << "[" << session_id << "] " << "FIX Session Created" << std::endl;
                session_id_map_[session_id.toString()] = session_id;
            }

            void FixAdaptorClient::onLogon(const FIX::SessionID& session_id)
            {
                std::cout << "[" << session_id << "] " << "Logged on" << std::endl;
                if (!connected_)
                {
                    std::cout << "Security def mode: " << security_def_mode_ ? "true" : "false";
                    if (security_def_mode_ == true)
                    {
                        this->send_security_def_request();
                    }
                    connected_ = true;

                    std::vector<std::string> instr =
                    {
                        "00A0FQ00GEZ",
                        "00A0IQ00GEZ",
                        "00A0LQ00GEZ",
                        "00A0LR00GEZ",
                        "00A0LS00GEZ",
                        "00A0CQ00GEZ",
                        "00A0CR00GEZ",
                        "00A0CS00GEZ",
                        "00A0FR00GEZ",
                        "00A0CV00GEZ",
                        "00A0IT00GEZ",
                        "00A0FU00GEZ",
                        "00A0FS00GEZ",
                        "00A0CX00GEZ", 
                        "00A0IY00GEZ",
                        "00A0LX00GEZ",
                        "00A0LP00GEZ",
                        "00A0IP00GEZ",
                        "00A0FP00GEZ",
                        "00A0CT00GEZ",
                        "00A0CU00GEZ",
                        "00A0CW00GEZ",
                        "00A0CY00GEZ",
                        "00A0FT00GEZ",
                        "00A0FV00GEZ",
                        "00A0FW00GEZ",
                        "00A0FX00GEZ",
                        "00A0FY00GEZ",
                        "00A0GP00GEZ",
                        "00A0HP00GEZ",
                        "00A0IR00GEZ",
                        "00A0IS00GEZ",
                        "00A0IU00GEZ",
                        "00A0IV00GEZ",
                        "00A0IW00GEZ",
                        "00A0IX00GEZ",
                        "00A0JP00GEZ",
                        "00A0KP00GEZ",
                        "00A0LT00GEZ",
                        "00A0LU00GEZ",
                        "00A0LV00GEZ",
                        "00A0LW00GEZ",
                        "00A0LY00GEZ",
                        "01A0CA00GEZ",
                        "01A0FA00GEZ"
                    };

                    auto sid = session_id_map_[Constants::PRICE_SESSION_ID];
                    
                    for (auto & inst : instr)
                    {
                        auto req = message_factory_.build_mktdata_bbo_subscribe(inst);
                        FIX::Session::sendToTarget(*req, sid);
                    }
                }

            }

            void FixAdaptorClient::onLogout(const FIX::SessionID& session_id)
            {
                std::cout << "[" << session_id << "] " << "Logged out" << std::endl;
            }

            void FixAdaptorClient::toAdmin(FIX::Message& message, const FIX::SessionID& session_id)
            {
                FIX::MsgType msg_type;
                message.getHeader().getField(msg_type);

                if (require_password_ && msg_type == "A")
                {
                    std::cout << "[" << session_id << "] " << "Setting password" << std::endl;
                    message.setField(password_field_, password_);
                }
            }

            void FixAdaptorClient::toApp(FIX::Message& message,
                const FIX::SessionID& session_id) throw(FIX::DoNotSend)
            {
                std::cout << "[" << session_id << "] " << message << std::endl;
            }

            void FixAdaptorClient::fromAdmin(const FIX::Message& message, const FIX::SessionID& session_id)
                throw(FIX::FieldNotFound, FIX::IncorrectDataFormat, FIX::IncorrectTagValue, FIX::RejectLogon)
            {
                std::cout << "[" << session_id << "] " << message << std::endl;
            }

            void FixAdaptorClient::fromApp(const FIX::Message& message, const FIX::SessionID& session_id)
                throw(FIX::FieldNotFound, FIX::IncorrectDataFormat,
                FIX::IncorrectTagValue, FIX::UnsupportedMessageType)
            {
                std::cout << "[" << session_id << "] " << message << std::endl;
                crack(message, session_id);
            }

            void FixAdaptorClient::onMessage(const FIX42::SecurityDefinition& message, const FIX::SessionID& session_id)
            {
                //std::cout << "[" << session_id << "] " << message << std::endl;
            }

            void FixAdaptorClient::onMessage(const FIX42::MarketDataSnapshotFullRefresh& message,
                const FIX::SessionID& session_id)
            {
                std::cout << "[" << session_id << "] " << message << std::endl;
            }

            void FixAdaptorClient::login(FIX::SessionID& session_id)
            {

            }

            void FixAdaptorClient::logout(FIX::SessionID& session_id)
            {

            }

            void FixAdaptorClient::send_security_def_request()
            {
                auto sec_def_request = message_factory_.build_secdef_request_pdt("FUT");
                sec_def_request->setField(Constants::REQUEST_TICK_TABLE_FIELD, "Y");
                auto sid = session_id_map_[Constants::PRICE_SESSION_ID];
                FIX::Session::sendToTarget(*sec_def_request, sid);
                std::cout << "[" << "FIX.4.2:KAPILDTS->TTDEV13P" << "] " << "Sending message - " << *sec_def_request << std::endl;
            }

            void FixAdaptorClient::start()
            {
                try
                {
                    session_settings_ = std::unique_ptr<FIX::SessionSettings>(new FIX::SessionSettings(config_filename_));
                    file_log_factory_ = std::unique_ptr<FIX::FileLogFactory>(new FIX::FileLogFactory(*session_settings_));
                    file_store_factory_ = std::unique_ptr<FIX::FileStoreFactory>(new FIX::FileStoreFactory(*session_settings_));

                    socket_initiator_ =
                        std::unique_ptr<FIX::SocketInitiator>(
                        new FIX::SocketInitiator(*this,
                        *file_store_factory_,
                        *session_settings_,
                        *file_log_factory_));

                    socket_initiator_->start();
                }
                catch (const std::exception& ex)
                {
                    std::cout << ex.what() << std::endl;
                }
            }

            void FixAdaptorClient::stop()
            {
                connected_ = false;
                socket_initiator_->stop();
            }
        }
    }
}