package com.atguigu.msmservice.service;

import java.util.HashMap;

public interface MsmService {
    boolean send(HashMap<String, Object> map, String phone);
}
