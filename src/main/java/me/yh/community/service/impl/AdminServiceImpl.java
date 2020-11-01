package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.config.security.CustomUser;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.AdminService;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor

@Transactional(readOnly = true)
@Service
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final SessionRegistry sessionRegistry;

    @Transactional
    @Override
    public boolean modifyMember(String id, Map<String, Object> map) {

        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isEmpty())
            return false;
        Member member = byId.get();

        String type = (String) map.get("type");   //수정할 타입 enable or role
        if (type.equals("role")) {
            String role = (String) map.get("role");
            member.setRole(role);
        } else if (type.equals("enable")) {
            String bool = (String) map.get("enable");
            boolean enable = Boolean.parseBoolean(bool);
            member.setEnable(enable);
        }
        removeSession(id);

        return true;
    }

    private void removeSession(String id) {

        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        allPrincipals.forEach(o -> {
            if(o instanceof User) {
                CustomUser customUser = (CustomUser) o;

                if (customUser.getUsername().equals(id)) {
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(o, false);

                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                        }
                    }
                }
            }
        });
    }
}
