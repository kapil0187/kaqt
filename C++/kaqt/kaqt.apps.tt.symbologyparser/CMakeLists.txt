add_executable(kaqt.apps.tt.symbologyparser ../src/kaqt/apps/tt/symbology/main.cpp)
target_link_libraries(kaqt.apps.tt.symbologyparser kaqt ${QUICKFIX_LIBRARIES} ${Boost_LIBRARIES} ${PROTOBUF_LIBRARIES})
