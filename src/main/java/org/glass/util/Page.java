/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.glass.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A page of results.
 *
 * @author damien bourdette
 */
public class Page<E> {
    /**
     * Query that have been used to get this page.
     */
    private Query query;

    /**
     * All items in current page.
     */
    private List<E> items = new ArrayList<E>();

    /**
     * Total count of items in store.
     */
    private int totalCount;

    private Page() {
        super();
    }

    public static <E> Page<E> fromQuery(Query query) {
        Page<E> page = new Page<E>();

        page.query = query;

        return page;
    }

    public int getCount() {
        if (totalCount == 0) {
            return 1;
        }

        int pageCount = totalCount / query.getSize();

        if (pageCount * query.getSize() != totalCount) {
            pageCount += 1;
        }

        return pageCount;
    }

    public Query getQuery() {
        return query;
    }

    public List<E> getItems() {
        return new ArrayList<E>(items);
    }

    public void setItems(List<E> items) {
        this.items = new ArrayList<E>(items);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
