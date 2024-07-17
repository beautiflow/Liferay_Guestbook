/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.sdr.guestbook.service.impl;

import com.liferay.portal.aop.AopService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.sdr.guestbook.exception.GuestbookEntryEmailException;
import com.sdr.guestbook.exception.GuestbookEntryMessageException;
import com.sdr.guestbook.exception.GuestbookEntryNameException;
import com.sdr.guestbook.model.GuestbookEntry;
import com.sdr.guestbook.service.base.GuestbookEntryLocalServiceBaseImpl;

import org.osgi.service.component.annotations.Component;

import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.util.Validator;

/**
 * @author beautiflow
 */
@Component(
        property = "model.class.name=com.sdr.guestbook.model.GuestbookEntry",
        service = AopService.class
)
public class GuestbookEntryLocalServiceImpl extends GuestbookEntryLocalServiceBaseImpl {

    // 게시물을 테이블에 추가하는 메서드
    public GuestbookEntry addGuestbookEntry(long userId, long guestbookId, String name, String email, String message, ServiceContext serviceContext)
            throws PortalException {

        long groupId = serviceContext.getScopeGroupId();

        User user = userLocalService.getUserById(userId);

        Date now = new Date();
        validate(name, email, message);

        long entryId = counterLocalService.increment();

        GuestbookEntry entry = guestbookEntryPersistence.create(entryId);

        entry.setUuid(serviceContext.getUuid());
        entry.setUserId(userId);
        entry.setGroupId(groupId);
        entry.setCompanyId(user.getCompanyId());
        entry.setUserName(user.getFullName());
        entry.setCreateDate(serviceContext.getCreateDate(now));
        entry.setModifiedDate(serviceContext.getModifiedDate(now));
        entry.setExpandoBridgeAttributes(serviceContext);
        entry.setGuestbookId(guestbookId);
        entry.setName(name);
        entry.setEmail(email);
        entry.setMessage(message);

        guestbookEntryPersistence.update(entry);

        return entry;
    }

    // 제출한 게시물 수정하는 메서드
    public GuestbookEntry updateGuestbookEntry(long userId, long guestbookId, long entryId, String name, String email, String message, ServiceContext serviceContext)
            throws PortalException, SystemException {

        Date now = new Date();

        validate(name, email, message);

        GuestbookEntry entry = guestbookEntryPersistence.findByPrimaryKey(entryId);

        User user = userLocalService.getUserById(userId);

        entry.setUserId(userId);
        entry.setUserName(user.getFullName());
        entry.setModifiedDate(serviceContext.getModifiedDate(now));
        entry.setName(name);
        entry.setEmail(email);
        entry.setMessage(message);
        entry.setExpandoBridgeAttributes(serviceContext);

        guestbookEntryPersistence.update(entry);

        return entry;
    }

    // entryId 나 개체를 사용하여 항목을 삭제하는 메서드
    public GuestbookEntry deleteGuestbookEntry(GuestbookEntry entry) throws PortalException {
        guestbookEntryPersistence.remove(entry);
        return entry;
    }

    // entryId 를 제공하면 개체가 검색되어 개체를 삭제하는 메서드
    public GuestbookEntry deleteGuestbookEntry(long entryId) throws PortalException {
        GuestbookEntry entry = guestbookEntryPersistence.findByPrimaryKey(entryId);
        return deleteGuestbookEntry(entry);
    }

    // 항목 목록을 가져오는 메서드
    public List<GuestbookEntry> getGuestbookEntries(long groupId, long guestbookId) {
        return guestbookEntryPersistence.findByG_G(groupId, guestbookId);
    }

    // 페이지가 매겨진 목록을 가져오는 메서드
    public List<GuestbookEntry> getGuestbookEntries(long groupId, long guestbookId, int start, int end) throws SystemException {
        return guestbookEntryPersistence.findByG_G(groupId, guestbookId, start, end);
    }

    // 페이지가 매겨진 목록을 정렬하는 메서드
    public List<GuestbookEntry> getGuestbookEntries(long groupId, long guestbookId, int start, int end, OrderByComparator<GuestbookEntry> obc) {
        return guestbookEntryPersistence.findByG_G(groupId, guestbookId, start, end, obc);
    }

    public GuestbookEntry getGuestbookEntry(long entryId) throws PortalException {
        return guestbookEntryPersistence.findByPrimaryKey(entryId);
    }

    // 총 항목 수를 정수로 가져오는 메서드
    public int getGuestookEntriesCount(long groupId, long guestbookId) {
        return guestbookEntryPersistence.countByG_G(groupId, guestbookId);
    }

    // 유효성 검증 메서드
    protected void validate(String name, String email, String entry) throws PortalException {

        if (Validator.isNull(name)) {
            System.out.println("name = " + name);;
            throw new GuestbookEntryNameException();
        }

        if (!Validator.isEmailAddress(email)) {
            throw new GuestbookEntryEmailException();
        }

        if (Validator.isNull(entry)) {
            System.out.println("entry = " + entry);
            throw new GuestbookEntryMessageException();
        }
    }


}
