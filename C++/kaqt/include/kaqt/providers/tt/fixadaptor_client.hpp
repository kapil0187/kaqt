#ifndef KAQT_PROVIDERS_TT_FIXADAPTOR_CLIENT_HPP_
#define KAQT_PROVIDERS_TT_FIXADAPTOR_CLIENT_HPP_

#include <iostream>
#include <string>
#include <memory>
#include <thread>
#include <map>

#include <kaqt/foundation/id_generator.hpp>
#include <kaqt/providers/quickfix/fix42/message_factory.hpp>

#include <quickfix/Application.h>
#include <quickfix/MessageCracker.h>
#include <quickfix/SocketInitiator.h>
#include <quickfix/SessionSettings.h>
#include <quickfix/SessionID.h>
#include <quickfix/Session.h>
#include <quickfix/Message.h>
#include <quickfix/FileLog.h>
#include <quickfix/FileStore.h>
#include <quickfix/Field.h>

#include <quickfix/fix42/SecurityDefinition.h>
#include <quickfix/fix42/SecurityDefinitionRequest.h>
#include <quickfix/fix42/MarketDataRequest.h>
#include <quickfix/fix42/MarketDataSnapshotFullRefresh.h>

namespace kaqt
{
    namespace providers
    {
        namespace tt
        {
            class FixAdaptorClient : public FIX::Application, public FIX::MessageCracker
            {
            private:
                std::string config_filename_;
                std::string password_;
                bool security_def_mode_;
                bool require_password_;
                int password_field_;
                bool connected_;
                kaqt::providers::quickfix::fix42::MessageFactory message_factory_;
                std::unique_ptr<FIX::SessionSettings> session_settings_;
                std::unique_ptr<FIX::FileStoreFactory> file_store_factory_;
                std::unique_ptr<FIX::FileLogFactory> file_log_factory_;
                std::unique_ptr<FIX::SocketInitiator> socket_initiator_;
                std::map<std::string, FIX::SessionID> session_id_map_;
            public:
                FixAdaptorClient(const FixAdaptorClient& source) = delete;
                FixAdaptorClient(FixAdaptorClient&& source) = delete;
                FixAdaptorClient& operator=(const FixAdaptorClient& source) = delete;
                FixAdaptorClient& operator=(FixAdaptorClient&& source);
                FixAdaptorClient(const std::string& config_filename, 
                    const std::string& password, const int password_field,
                    const bool security_def_mode);
                ~FixAdaptorClient();
                void onCreate(const FIX::SessionID& session_id);
                void onLogon(const FIX::SessionID& session_id);
                void onLogout(const FIX::SessionID& session_id);
                void toAdmin(FIX::Message& message, const FIX::SessionID& session_id);
                void toApp(FIX::Message& message, const FIX::SessionID& session_id) throw(FIX::DoNotSend);
                void fromAdmin(const FIX::Message& message, const FIX::SessionID& session_id)
                    throw(FIX::FieldNotFound, FIX::IncorrectDataFormat,
                    FIX::IncorrectTagValue, FIX::RejectLogon);
                void fromApp(const FIX::Message& message, const FIX::SessionID& session_id)
                    throw(FIX::FieldNotFound, FIX::IncorrectDataFormat,
                    FIX::IncorrectTagValue, FIX::UnsupportedMessageType);
                void onMessage(const FIX42::SecurityDefinition& message, const FIX::SessionID& session_id);
                void onMessage(const FIX42::MarketDataSnapshotFullRefresh& message, const FIX::SessionID& session_id);
                void login(FIX::SessionID& session_id);
                void logout(FIX::SessionID& session_id);
                void send_security_def_request();
                bool get_connection_status()
                {
                    return connected_;
                }
                void start();
                void stop();
            };

        }
    }
}

#endif // KAQT_PROVIDERS_TT_FIXADAPTOR_CLIENT_HPP_