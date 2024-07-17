package com.sdr.guestbook.user.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.sdr.guestbook.constants.GuestbookConstants;
import com.sdr.guestbook.model.Guestbook;
import com.sdr.guestbook.model.GuestbookEntry;
import com.sdr.guestbook.service.GuestbookEntryLocalServiceUtil;
import com.sdr.guestbook.service.GuestbookLocalServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.util.List;





@Controller
@RequestMapping("VIEW")
public class GuestbookController {
    private static final Log _log = LogFactoryUtil.getLog(GuestbookController.class);
    private static final Logger log = LoggerFactory.getLogger(GuestbookController.class);

    // 방명록 항목 표시 메서드
    @RenderMapping
    public String view(RenderRequest renderRequest, RenderResponse renderResponse, Model model) {
        _log.debug("GuestbookController : view");

        try {
            userVerification(renderRequest);

            ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), renderRequest);
            long groupId = serviceContext.getScopeGroupId();
            long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

            List<Guestbook> guestbooks = GuestbookLocalServiceUtil.getGuestbooks(groupId);

            if (guestbooks.isEmpty()) {
                Guestbook guestbook = GuestbookLocalServiceUtil.addGuestbook(serviceContext.getUserId(), "Main", serviceContext);
                guestbookId = guestbook.getGuestbookId();
            }

            if (guestbookId == 0) {
                guestbookId = guestbooks.get(0).getGuestbookId();
            }

            model.addAttribute("guestbookId", guestbookId);
            model.addAttribute("guestbooks", guestbooks);

        /*
            Entries
        */
            List<GuestbookEntry> guestbookEntries = GuestbookEntryLocalServiceUtil.getGuestbookEntries(groupId, guestbookId);
            model.addAttribute("guestbookEntries", guestbookEntries);
        } catch (Exception e) {
            // TODO 오류 처리 필요
            e.printStackTrace();
        }
        _log.info(GuestbookConstants.PORTLET_NAME + "12341234");

        return "guestbook/user/view";
    }

    private void userVerification(PortletRequest portletRequest) {
        try {
            User user = PortalUtil.getUser(portletRequest);
            if (user == null) {
                throw new PrincipalException();
            }
        } catch (PortalException e) {
            // TO do 사용자 인증 오류 처리 필요
            _log.error("사용자 인증 오류");
            e.printStackTrace();
        }
    }

    // edit-entry 페이지로 이동하는 메서드
    @RenderMapping(params = "action=editEntry")
    public String editEntry(RenderRequest renderRequest, Model model) {
        _log.debug("GuestbookController : editEntry");

        try {
            userVerification(renderRequest);

            long entryId = ParamUtil.getLong(renderRequest, "entryId");
            long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

            GuestbookEntry entry = null;
            if (entryId > 0) {
                entry = GuestbookEntryLocalServiceUtil.getGuestbookEntry(entryId);
            }

            model.addAttribute("entry", entry);
            model.addAttribute("guestbookId", guestbookId);

        } catch (PortalException e) {
            // TODO 오류 처리 필요
            e.printStackTrace();
        }
        return "guestbook/user/edit_entry";
    }




    // 사용자가 jsp 에 제출하는 데이터를 가져와 항목 데이터로 저장되도록 서비스에 전달하는 메서드
    @ActionMapping(params = "action=saveEntry")
    public void saveEntry(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException {
        _log.debug("GuestbookController : saveEntry");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(GuestbookEntry.class.getName(), actionRequest);

        userVerification(actionRequest);

        String userName = ParamUtil.getString(actionRequest, "name");
        String email = ParamUtil.getString(actionRequest, "email");
        String message = ParamUtil.getString(actionRequest, "message");
        long guestbookId = ParamUtil.getLong(actionRequest, "guestbookId");
        long entryId = ParamUtil.getLong(actionRequest, "entryId");

        try {
            if (entryId > 0) {
                GuestbookEntryLocalServiceUtil.updateGuestbookEntry(serviceContext.getUserId(), guestbookId, entryId, userName, email, message, serviceContext);
                // 메시지 추가
                SessionMessages.add(actionRequest, "entryUpdated");
            } else {
                GuestbookEntryLocalServiceUtil.addGuestbookEntry(serviceContext.getUserId(), guestbookId, userName, email, message, serviceContext);
                // 메시지 추가
                SessionMessages.add(actionRequest, "entryAdded");
            }
        } catch (Exception e) {
            PortalUtil.copyRequestParameters(actionRequest, actionResponse);
            e.printStackTrace();
            // 메시지 추가
            SessionErrors.add(actionRequest, e.getClass().getName());
            e.getMessage();

        }
        actionResponse.setRenderParameter("guestbookId", Long.toString(guestbookId));
    }

    // 항목을 삭제하는 메서드
    @ActionMapping(params = "action=deleteEntry")
    public void deleteEntry(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException {
        _log.debug("GuestbookController : deleteEntry");

        long entryId = ParamUtil.getLong(actionRequest, "entryId");
        long guestbookId = ParamUtil.getLong(actionRequest, "guestbookId");

        try {
            actionResponse.setRenderParameter("guestbookId", Long.toString(guestbookId));
            GuestbookEntryLocalServiceUtil.deleteGuestbookEntry(entryId);
            // 메시지 추가
            SessionMessages.add(actionRequest, "entryDeleted");
        } catch (Exception e) {
            e.printStackTrace();
            // 메시지 추가
            SessionErrors.add(actionRequest, e.getClass().getName());

        }

    }


}
























