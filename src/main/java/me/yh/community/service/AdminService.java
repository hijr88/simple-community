package me.yh.community.service;

import java.util.Map;

public interface AdminService {
    boolean modifyMember(String id, Map<String, Object> map);
}
