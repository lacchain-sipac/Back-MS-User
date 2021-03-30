package com.invest.honduras.util;


import java.util.Iterator;
import java.util.List;

import com.invest.honduras.http.request.RoleRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListUtil {

	public static void removeSimilarRole(final List<RoleRequest> rolesOld, final List<RoleRequest> rolesNew) {
		try {

			for (Iterator<RoleRequest> roleOldIterator = rolesOld.iterator(); roleOldIterator.hasNext();) {

				RoleRequest roleOld = roleOldIterator.next();

				for (Iterator<RoleRequest> roleNewIterator = rolesNew.iterator(); roleNewIterator.hasNext();) {

					RoleRequest roleNew = roleNewIterator.next();

					if (roleOld.getCode().equals(roleNew.getCode())) {
						roleOldIterator.remove();
						roleNewIterator.remove();
					}
				}
			}

		} catch (Exception e) {
			log.error("ListUtil.removeSimilarRole", e);
		}
	}

}
