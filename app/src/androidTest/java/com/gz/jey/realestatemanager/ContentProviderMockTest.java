package com.gz.jey.realestatemanager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;

import com.gz.jey.realestatemanager.provider.RealEstateContentProvider;

import java.util.HashMap;

public class ContentProviderMockTest extends AndroidTestCase {

    private Uri uri = Uri.parse("content://"+RealEstateContentProvider.AUTHORITY+"/"+RealEstateContentProvider.CommandEntry.TABLE_NAME);
    public void testMockContentProvider(){
        //Step 1: Create data you want to return and put it into a matrix cursor
        //In this case I am mocking getting ids from RealEstateContentProvider
        String[] exampleData = {"987","1490000","750"};
        String[] exampleProjection = new String[] { RealEstateContentProvider.CommandEntry.COLUMN_ID, RealEstateContentProvider.CommandEntry.COLUMN_PRICE, RealEstateContentProvider.CommandEntry.COLUMN_SURFACE};
        Cursor matrixCursor = new MatrixCursor(exampleProjection);
        ((MatrixCursor) matrixCursor).addRow(exampleData);

        //Step 2: Create a stub content provider and add the matrix cursor as the expected result of the query
        HashMapMockContentProvider mockProvider = new HashMapMockContentProvider();
        mockProvider.addQueryResult(uri, matrixCursor);

        //Step 3: Create a mock resolver and add the content provider.
        MockContentResolver mockResolver = new MockContentResolver();
        mockResolver.addProvider(RealEstateContentProvider.AUTHORITY , mockProvider);

        //Step 4: Add the mock resolver to the mock context
        ContextWithMockContentResolver mockContext = new ContextWithMockContentResolver(super.getContext());
        mockContext.setContentResolver(mockResolver);

        //Example Test
        ExampleClassUnderTest underTest = new ExampleClassUnderTest();
        String result = underTest.getId(mockContext);

        assertEquals("987",result);
    }

    //Specialized Mock Content provider for step 2.  Uses a hashmap to return data dependent on the uri in the query
    public class HashMapMockContentProvider extends MockContentProvider {
        private HashMap<Uri, Cursor> expectedResults = new HashMap<Uri, Cursor>();
        public void addQueryResult(Uri uriIn, Cursor expectedResult){
            expectedResults.put(uriIn, expectedResult);
        }
        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
            return expectedResults.get(uri);
        }
    }

    public class ContextWithMockContentResolver extends RenamingDelegatingContext {
        private ContentResolver contentResolver;
        public void setContentResolver(ContentResolver contentResolver){ this.contentResolver = contentResolver;}
        public ContextWithMockContentResolver(Context targetContext) { super(targetContext, "test");}
        @Override public ContentResolver getContentResolver() { return contentResolver; }
        @Override public Context getApplicationContext(){ return this; } //Added in-case my class called getApplicationContext()
    }

    //An example class under test which queries the populated cursor to get the expected id
    public class ExampleClassUnderTest{
        public  String getId(Context context){//Query for  ids from RealEstateContentProvider
            String[] projection = new String[]{ RealEstateContentProvider.CommandEntry.COLUMN_ID};
            Cursor cursor= context.getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToNext();
            return cursor.getString(cursor.getColumnIndex(RealEstateContentProvider.CommandEntry.COLUMN_ID));
        }
    }
}
