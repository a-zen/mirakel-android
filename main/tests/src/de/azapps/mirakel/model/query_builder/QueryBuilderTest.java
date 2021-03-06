/*******************************************************************************
 * Mirakel is an Android App for managing your ToDo-Lists
 *
 * Copyright (c) 2013-2014 Anatolij Zelenin, Georg Semmler.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.azapps.mirakel.model.query_builder;

import android.database.Cursor;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.common.base.Optional;

import java.util.Arrays;
import java.util.List;

import de.azapps.mirakel.model.ModelBase;
import de.azapps.mirakel.model.account.AccountMirakel;
import de.azapps.mirakel.model.file.FileMirakel;
import de.azapps.mirakel.model.list.ListMirakel;
import de.azapps.mirakel.model.list.SpecialList;
import de.azapps.mirakel.model.query_builder.MirakelQueryBuilder.Operation;
import de.azapps.mirakel.model.recurring.Recurring;
import de.azapps.mirakel.model.semantic.Semantic;
import de.azapps.mirakel.model.task.Task;
import de.azapps.mirakelandroid.test.MirakelTestCase;
import de.azapps.mirakelandroid.test.RandomHelper;

public class QueryBuilderTest extends MirakelTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RandomHelper.init(getContext());
    }

    @SmallTest
    public void testBasicQuery() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        assertEquals("", qb.getSelection());
    }

    @SmallTest
    public void testSingleAndArgument() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.EQ, 1);
        assertEquals(ModelBase.ID + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    @SmallTest
    public void testSingleOrArgument() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.or(ModelBase.ID, Operation.EQ, 1);
        assertEquals(ModelBase.ID + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    @SmallTest
    public void testEqual() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkOperation(qb, Operation.EQ, "=");
    }

    @SmallTest
    public void testGreaterEqual() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkOperation(qb, Operation.GE, ">=");
    }

    @SmallTest
    public void testLesserEqual() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkOperation(qb, Operation.LE, "<=");
    }

    @SmallTest
    public void testGreater() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkOperation(qb, Operation.GT, ">");
    }

    @SmallTest
    public void testLesser() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkOperation(qb, Operation.LT, "<");
    }

    @SmallTest
    public void testLike() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkOperation(qb, Operation.LIKE, "LIKE");
    }

    @SmallTest
    public void testIn() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.IN, 1);
        assertEquals(ModelBase.ID + " IN(?)", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    private static void checkOperation(final MirakelQueryBuilder qb,
                                       final Operation op, final String o) {
        qb.and(ModelBase.ID, op, 1);
        assertEquals(ModelBase.ID + " " + o + " ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    @SmallTest
    public void testNotEqual() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkNotOperation(qb, Operation.NOT_EQ, "=");
    }

    @SmallTest
    public void testNotGreaterEqual() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkNotOperation(qb, Operation.NOT_GE, ">=");
    }

    @SmallTest
    public void testNotLesserEqual() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkNotOperation(qb, Operation.NOT_LE, "<=");
    }

    @SmallTest
    public void testNotGreater() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkNotOperation(qb, Operation.NOT_GT, ">");
    }

    @SmallTest
    public void testNotLesser() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkNotOperation(qb, Operation.NOT_LT, "<");
    }

    @SmallTest
    public void testNotLike() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        checkNotOperation(qb, Operation.NOT_LIKE, "LIKE");
    }

    @SmallTest
    public void testNotIn() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.NOT_IN, 1);
        assertEquals("NOT " + ModelBase.ID + " IN(?)", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    private static void checkNotOperation(final MirakelQueryBuilder qb,
                                          final Operation op, final String o) {
        qb.and(ModelBase.ID, op, 1);
        assertEquals("NOT " + ModelBase.ID + " " + o + " ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    @SmallTest
    public void testTwoAndConditions() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.EQ, 1).and(ModelBase.NAME, Operation.EQ,
                "foo");
        assertEquals(ModelBase.ID + " = ? AND " + ModelBase.NAME + " = ?",
                     qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(2, args.size());
        assertEquals("1", args.get(0));
        assertEquals("foo", args.get(1));
    }

    @SmallTest
    public void testTwoOrConditions() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.EQ, 1).or(ModelBase.NAME, Operation.EQ,
                "foo");
        assertEquals(ModelBase.ID + " = ? OR " + ModelBase.NAME + " = ?",
                     qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(2, args.size());
        assertEquals("1", args.get(0));
        assertEquals("foo", args.get(1));
    }

    @SmallTest
    public void testAndSubcondition() {
        final MirakelQueryBuilder qbInner = new MirakelQueryBuilder(
            getContext());
        qbInner.and(ModelBase.ID, Operation.EQ, 1).or(ModelBase.NAME,
                Operation.EQ, "foo");
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(Task.DONE, Operation.EQ, false).and(qbInner);
        assertEquals(Task.DONE + " = ? AND (" + ModelBase.ID + " = ? OR "
                     + ModelBase.NAME + " = ?)", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(3, args.size());
        assertEquals("0", args.get(0));
        assertEquals("1", args.get(1));
        assertEquals("foo", args.get(2));
    }

    @SmallTest
    public void testOrSubcondition() {
        final MirakelQueryBuilder qbInner = new MirakelQueryBuilder(
            getContext());
        qbInner.and(ModelBase.ID, Operation.EQ, 1).or(ModelBase.NAME,
                Operation.EQ, "foo");
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.or(Task.DONE, Operation.EQ, false).or(qbInner);
        assertEquals(Task.DONE + " = ? OR (" + ModelBase.ID + " = ? OR "
                     + ModelBase.NAME + " = ?)", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(3, args.size());
        assertEquals("0", args.get(0));
        assertEquals("1", args.get(1));
        assertEquals("foo", args.get(2));
    }

    @SmallTest
    public void testSubquery() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        final MirakelQueryBuilder qbInner = new MirakelQueryBuilder(
            getContext()).select(ModelBase.ID);
        qb.and(ModelBase.ID, Operation.IN, qbInner, Task.URI);
        assertEquals(ModelBase.ID + " IN (SELECT " + ModelBase.ID + " FROM "
                     + Task.TABLE + ")", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(0, args.size());
    }

    @SmallTest
    public void testBooleanFilterTrue() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(Task.DONE, Operation.EQ, true);
        assertEquals(Task.DONE + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    @SmallTest
    public void testBooleanFilterFalse() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(Task.DONE, Operation.EQ, false);
        assertEquals(Task.DONE + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("0", args.get(0));
    }

    @SmallTest
    public void testIntFilter() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.EQ, 1);
        assertEquals(ModelBase.ID + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1", args.get(0));
    }

    @SmallTest
    public void testDoubleFilter() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(Task.PROGRESS, Operation.EQ, 1.0);
        assertEquals(Task.PROGRESS + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("1.0", args.get(0));
    }

    @SmallTest
    public void testStringFilter() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.NAME, Operation.EQ, "foo");
        assertEquals(ModelBase.NAME + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals("foo", args.get(0));
    }

    @SmallTest
    public void testModelBaseFilter() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        final Task t = RandomHelper.getRandomTask();
        qb.and(ModelBase.ID, Operation.EQ, t);
        assertEquals(ModelBase.ID + " = ?", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(1, args.size());
        assertEquals(t.getId() + "", args.get(0));
    }

    @SmallTest
    public void testListIntegerFilter() {
        final List<Integer> filter = Arrays
                                     .asList(new Integer[] { 1, 2, 3, 42 });
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.ID, Operation.IN, filter);
        assertEquals(ModelBase.ID + " IN(?,?,?,?)", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(filter.size(), args.size());
        for (int i = 0; i < filter.size(); i++) {
            assertEquals(filter.get(i) + "", args.get(i));
        }
    }

    @SmallTest
    public void testListStringFilter() {
        final List<String> filter = Arrays.asList(new String[] { "foo", "bar",
                                    "don't do this"
                                                               });
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        qb.and(ModelBase.NAME, Operation.IN, filter);
        assertEquals(ModelBase.NAME + " IN(?,?,?)", qb.getSelection());
        final List<String> args = qb.getSelectionArguments();
        assertEquals(filter.size(), args.size());
        for (int i = 0; i < filter.size(); i++) {
            assertEquals(filter.get(i), args.get(i));
        }
    }

    //test get
    @MediumTest
    public void testGetTask() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<Task> res_qb = qb.get(Task.class);
        Cursor c = getContext().getContentResolver().query(Task.URI, Task.allColumns, null,
                   null, null);
        c.moveToFirst();
        Task res_raw = new Task(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }
    @MediumTest
    public void testGetAccount() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<AccountMirakel> res_qb = qb.get(AccountMirakel.class);
        Cursor c = getContext().getContentResolver().query(
                       AccountMirakel.URI, AccountMirakel.allColumns, null, null, null);
        c.moveToFirst();
        AccountMirakel res_raw = new AccountMirakel(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }
    @MediumTest
    public void testGetFile() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<FileMirakel> res_qb = qb.get(FileMirakel.class);
        Cursor c = getContext().getContentResolver().query(FileMirakel.URI,
                   FileMirakel.allColumns, null, null, null);
        c.moveToFirst();
        FileMirakel res_raw = new FileMirakel(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }
    @MediumTest
    public void testGetRecurring() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<Recurring> res_qb = qb.get(Recurring.class);
        Cursor c = getContext().getContentResolver().query(Recurring.URI,
                   Recurring.allColumns, null, null, null);
        c.moveToFirst();
        Recurring res_raw = new Recurring(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }
    @MediumTest
    public void testGetSemantic() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<Semantic> res_qb = qb.get(Semantic.class);
        Cursor c = getContext().getContentResolver().query(Semantic.URI,
                   Semantic.allColumns, null, null, null);
        c.moveToFirst();
        Semantic res_raw = new Semantic(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }
    @MediumTest
    public void testGetList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<ListMirakel> res_qb = qb.get(ListMirakel.class);
        Cursor c = getContext().getContentResolver().query(ListMirakel.URI,
                   ListMirakel.allColumns, null, null, null);
        c.moveToFirst();
        ListMirakel res_raw = new ListMirakel(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }
    @MediumTest
    public void testGetMetaList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        Optional<SpecialList> res_qb = qb.get(SpecialList.class);
        Cursor c = getContext().getContentResolver().query(SpecialList.URI,
                   SpecialList.allColumns, null, null, null);
        c.moveToFirst();
        SpecialList res_raw = new SpecialList(c);
        c.close();
        assertTrue(res_qb.isPresent());
        assertEquals(res_raw, res_qb.get());
    }

    //test getList
    private void compareLists(List res_qb, List res_raw) {
        assertEquals(res_raw.size(), res_qb.size());
        for (int i = 0; i < res_raw.size(); i++) {
            assertEquals(res_raw.get(i), res_qb.get(i));
        }
    }
    @MediumTest
    public void testGetTaskList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(Task.class);
        List res_raw = Task.cursorToTaskList(getContext().getContentResolver().query(Task.URI,
                                             Task.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }



    @MediumTest
    public void testGetAccountList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(AccountMirakel.class);
        List res_raw = AccountMirakel.cursorToAccountList(getContext().getContentResolver().query(
                           AccountMirakel.URI, AccountMirakel.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }
    @MediumTest
    public void testGetFileList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(FileMirakel.class);
        List res_raw = FileMirakel.cursorToFileList(getContext().getContentResolver().query(FileMirakel.URI,
                       FileMirakel.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }
    @MediumTest
    public void testGetRecurringList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(Recurring.class);
        List res_raw = Recurring.cursorToList(getContext().getContentResolver().query(
                           Recurring.URI, Recurring.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }
    @MediumTest
    public void testGetSemanticList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(Semantic.class);
        List res_raw = Semantic.cursorToSemanticList(getContext().getContentResolver().query(Semantic.URI,
                       Semantic.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }
    @MediumTest
    public void testGetListList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(ListMirakel.class);
        List res_raw = ListMirakel.cursorToList(getContext().getContentResolver().query(ListMirakel.URI,
                                                ListMirakel.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }
    @MediumTest
    public void testGetMetaListList() {
        final MirakelQueryBuilder qb = new MirakelQueryBuilder(getContext());
        List res_qb = qb.getList(SpecialList.class);
        List res_raw = SpecialList.cursorToSpecialLists(getContext().getContentResolver().query(
                           SpecialList.URI, SpecialList.allColumns, null, null, null));
        compareLists(res_qb, res_raw);
    }

}
