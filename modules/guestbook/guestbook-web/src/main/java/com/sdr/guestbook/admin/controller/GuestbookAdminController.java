package com.sdr.guestbook.admin.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.sdr.guestbook.model.Guestbook;
import com.sdr.guestbook.service.GuestbookLocalServiceUtil;
import com.sdr.guestbook.utils.LocalUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;

@Controller
@RequestMapping("VIEW")
public class GuestbookAdminController {
    private static Log _log = LogFactoryUtil.getLog(GuestbookAdminController.class);

    @RenderMapping
    public String view(RenderRequest renderRequest, RenderResponse renderResponse, Model model) {
        _log.debug("GuestbookAdminController : view");

        try {
            LocalUtil.userVerification(renderRequest);

            ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), renderRequest);

            long groupId = serviceContext.getScopeGroupId();

            List<Guestbook> guestbooks = GuestbookLocalServiceUtil.getGuestbooks(groupId);

            model.addAttribute("guestbooks", guestbooks);
        }catch (Exception e) {
            // TO DO 오류 처리 필요
            e.printStackTrace();
        }


        return "guestbook/admin/view";
    }

    // 새 방명록을 추가하는 메서드
    @ActionMapping(params = "action=addGuestbook")
    public void addGuestbook(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException {
        ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), actionRequest);

        String name = ParamUtil.getString(actionRequest, "name");

        try{
            GuestbookLocalServiceUtil.addGuestbook(serviceContext.getUserId(), name, serviceContext);
            // 메시지 추가
            SessionMessages.add(actionRequest, "guestbookAdded");

        }catch(PortalException e){
            actionResponse.setRenderParameter("action", "addGuestbook");
//            response.setRenderParameter("action", "addGuestbook");
            e.printStackTrace();
            // 메시지 추가
            SessionErrors.add(actionRequest, e.getClass().getName());
        }
    }

    // 기존 방명록을 업데이트하기 위한 메서드
    @ActionMapping(params = "action=updateGuestbook")
    public void updateGuestbook(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), actionRequest);

        _log.info("GuestbookAdminController : updateGuestbook");
        _log.info(serviceContext.getUserId());

        String name = ParamUtil.getString(actionRequest, "name");
        long guestbookId = ParamUtil.getLong(actionRequest, "guestbookId");

        try {
            GuestbookLocalServiceUtil.updateGuestbook(serviceContext.getUserId(), guestbookId, name, serviceContext);
            // 메시지 추가
            SessionMessages.add(actionRequest, "guestbookUpdated");
        } catch (PortalException e) {
            actionResponse.setRenderParameter("action", "addGuestbook");
            e.printStackTrace();
            // 메시지 추가
            SessionErrors.add(actionRequest, e.getClass().getName());
        }
    }

    // 방명록을 삭제하기 위한 메서드
    @ActionMapping(params = "action=deleteGuestbook")
    public void deleteGuestbook(ActionRequest actionRequest) throws PortalException {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), actionRequest);

        long guestbookId = ParamUtil.getLong(actionRequest, "guestbookId");

        try {
            GuestbookLocalServiceUtil.deleteGuestbook(guestbookId, serviceContext);
            // 메시지 추가
            SessionMessages.add(actionRequest, "guestbookDeleted");

        } catch (PortalException e) {
            e.printStackTrace();
            // 메시지 추가
            SessionErrors.add(actionRequest, e.getClass().getName());
        }
    }

    // 새 방명록을 추가하고 기존 방명록을 편집하기 위한 메서드
    @RenderMapping(params = "action=editGuestbook")
    public String editGuestbook(RenderRequest renderRequest, Model model) {
        _log.debug("GuestbookAdminController : editGuestbook");

        long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

        Guestbook guestbook = null;

        if (guestbookId > 0) {
            try {
                guestbook = GuestbookLocalServiceUtil.getGuestbook(guestbookId);
            } catch (PortalException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("guestbook", guestbook);

        return "guestbook/admin/edit_guestbook";
    }












}

