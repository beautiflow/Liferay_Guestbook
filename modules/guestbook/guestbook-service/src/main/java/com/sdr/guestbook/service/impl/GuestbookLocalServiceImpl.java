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
import com.liferay.portal.kernel.util.Validator;
import com.sdr.guestbook.exception.GuestbookNameException;
import com.sdr.guestbook.model.Guestbook;
import com.sdr.guestbook.model.GuestbookEntry;
import com.sdr.guestbook.service.GuestbookEntryLocalService;
import com.sdr.guestbook.service.base.GuestbookLocalServiceBaseImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Date;
import java.util.List;


/**
 * @author beautiflow
 */
@Component(
        property = "model.class.name=com.sdr.guestbook.model.Guestbook",
        service = AopService.class
)
public class GuestbookLocalServiceImpl extends GuestbookLocalServiceBaseImpl {

    // 방명록을 테이블에 추가하는 메서드
    public Guestbook addGuestbook(long userId, String name, ServiceContext serviceContext) throws PortalException {
        long groupId = serviceContext.getScopeGroupId();

        User user = userLocalService.getUserById(userId);

        Date now = new Date();

        validate(name);

        long guestbookId = counterLocalService.increment();

        Guestbook guestbook = guestbookPersistence.create(guestbookId);

        guestbook.setUuid(serviceContext.getUuid());
        guestbook.setUserId(userId);
        guestbook.setGroupId(groupId);
        guestbook.setCompanyId(user.getCompanyId());
        guestbook.setUserName(user.getFullName());
        guestbook.setCreateDate(serviceContext.getCreateDate(now));
        guestbook.setModifiedDate(serviceContext.getModifiedDate(now));
        guestbook.setName(name);
        guestbook.setExpandoBridgeAttributes(serviceContext);

        guestbookPersistence.update(guestbook);

        return guestbook;
    }

    // groupId 조회하는 메서드
    public List<Guestbook> getGuestbooks(long groupId) {
        return guestbookPersistence.findByGroupId(groupId);
    }

    // groupId 로 지정된 사이트에서 방명록 목록을 검색하는 메서드
    public List<Guestbook> getGuestbooks(long groupId, int start, int end, OrderByComparator<Guestbook> obc) {
        return guestbookPersistence.findByGroupId(groupId, start, end, obc);
    }

    // 선택적으로 특정 순서로 페이지가 매겨진 목록을 가져오는 메서드
    public List<Guestbook> getGuestbooks(long groupId, int start, int end) {
        return guestbookPersistence.findByGroupId(groupId, start, end);
    }

    // 주어진 사이트에 대한 총 방명록 수를 제공하는 메서드
    public int getGuestbooksCount(long groupId) {
        return guestbookPersistence.countByGroupId(groupId);
    }

    // 방명록 유효성 검사기 메서드 : Liferay DXP의 Validator를 사용하여 사용자가 방명록 이름에 텍스트를 입력했는지 확인
    protected void validate(String name) throws PortalException {
        if (Validator.isNull(name)) {
            throw new GuestbookNameException();
        }
    }

    // 방명록을 업데이트를 위한 메서드 : ID 로 방명록을 검색하고 데이터를 사용자가 입력한 것으로 바꾼 다음 지속성 계층을 호출하여 데이터베이스에 다시 저장한다.
    public Guestbook updateGuestbook( long userId, long guestbookId, String name, ServiceContext serviceContext) throws PortalException, SystemException {
        System.out.println("GuestbookLocalServiceUtil.updateGuestbook 호출됨"); // 디버그 출력 추가

        Date now = new Date();
        validate(name);

        Guestbook guestbook = getGuestbook(guestbookId);
        User user = userLocalService.getUser(userId);

        guestbook.setUserId(userId);
        guestbook.setUserName(user.getFullName());
        guestbook.setModifiedDate(serviceContext.getModifiedDate(now));
        guestbook.setName(name);
        guestbook.setExpandoBridgeAttributes(serviceContext);

        guestbook = guestbookPersistence.update(guestbook);

        return guestbook;
    }



    // 방명록을 삭제하기 위한 메서드
    public Guestbook deleteGuestbook(long guestbookId, ServiceContext serviceContext) throws PortalException, SystemException {
        Guestbook guestbook = getGuestbook(guestbookId);

        List<GuestbookEntry> entries = _guestbookEntryLocalService.getGuestbookEntries(
                serviceContext.getScopeGroupId(), guestbookId);

        for (GuestbookEntry entry : entries) {
            _guestbookEntryLocalService.deleteGuestbookEntry(entry.getEntryId());
        }

      guestbook = deleteGuestbook(guestbook);
        return guestbook;
    }

    // GuestbookEntry 로컬 서비스에 대한 참조를 추가하여 deleteGuestbook 메서드에서 삽입하고 사용할 수 있도록 추가
    // 관례에 따라 Liferay 는 이를 클래스 맨 아래에 추가
    @Reference
    private GuestbookEntryLocalService _guestbookEntryLocalService;

}





























