add_executable(kaqt.app.tt ../src/kaqt/apps/tt/main.cpp)
target_link_libraries(kaqt.app.tt kaqt ${QUICKFIX_LIBRARIES} ${Boost_LIBRARIES} ${PROTOBUF_LIBRARIES} ${ZeroMQ_LIBRARIES})