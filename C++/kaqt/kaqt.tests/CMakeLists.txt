find_package(GTest REQUIRED)

if(GTest_FOUND)
  message("GTEST Found")
  include_directories(${GTEST_INCLUDE_DIRS})
endif()

add_executable(kaqt.tests ../src/kaqt/tests/main.cpp)

target_link_libraries(kaqt.tests kaqt ${QUICKFIX_LIBRARIES}
                                      ${Boost_LIBRARIES}
                                      ${PROTOBUF_LIBRARIES}
                                      ${ZeroMQ_LIBRARIES}
                                      ${GTEST_BOTH_LIBRARIES})
