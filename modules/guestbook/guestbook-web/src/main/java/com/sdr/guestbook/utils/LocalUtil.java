package com.sdr.guestbook.utils;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.PortletRequest;

public class LocalUtil {
    public static User userVerification(PortletRequest portletRequest) {
        User user = null;
        try {
            user = PortalUtil.getUser(portletRequest);
            if (user == null) {
                throw new PrincipalException();
            }
        } catch (PortalException e) {
            // TODO 오류 처리 필요
            e.printStackTrace();
        }
        return user;
    }
}
