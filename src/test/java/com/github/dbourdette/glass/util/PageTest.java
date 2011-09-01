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

package com.github.dbourdette.glass.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class PageTest {
    @Test
    public void count() {
        Query query = Query.oneBasedIndex(1).withSize(10);

        List<Long> longs = new ArrayList<Long>();
        for (int i = 1; i <= 45; i++) {
            longs.add(Long.valueOf(i));
        }

        Page page = Page.fromQuery(query);
        page.setItems(query.subList(longs));
        page.setTotalCount(45);

        Assert.assertEquals("There should be 5 pages", 5, page.getCount());
    }
}
