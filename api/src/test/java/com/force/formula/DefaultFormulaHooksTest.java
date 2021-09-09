/**
 * 
 */
package com.force.formula;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

import com.force.formula.util.FormulaFieldReferenceImpl;
import com.force.formula.util.SystemFormulaContext;
import com.force.i18n.grammar.GrammaticalLocalizer;

/**
 * Validate and the default implementation for FormulaEngineHooks
 *
 * @author stamm
 */
public class DefaultFormulaHooksTest  {

	@Test
    public void testEngineHooks() {
        FormulaEngineHooks def = new FormulaEngineHooks() {};
        Calendar cal = Calendar.getInstance();
        def.adjustCalendarForTestEnvironment(cal);

        FormulaTime time = def.constructTime(8640000);
        assertEquals(0, time.getMillisecond());
        assertEquals(8640000, time.getTimeInMillis());
        
        assertNull(def.constructGeolocation(null, null));
        
        assertNull(def.hook_unwrapForNullable(null));
        assertNull(def.hook_unwrapForNullable(new FormulaDataValue() {
			@Override
			public Object getNativeValue() {
				return "hi";
			}
			@Override
			public boolean isEmpty() {
				return true;
			}
		}));

        
        assertEquals("hi", def.hook_unwrapForNullable("hi"));
        
    }
    
    
	@Test
	public void testGetFieldReferenceValue() {
        FormulaEngineHooks def = new FormulaEngineHooks() {
			@Override
			public GrammaticalLocalizer getLocalizer() {
				return new GrammaticalLocalizer(Locale.US, Locale.US, null, null, null);
			}

			@Override
			public FormulaDataType getDataTypeByName(String name) {
				switch (name) {
				case "DateTime": return FormulaApiMocks.MockType.DATETIME;
				}
				return FormulaEngineHooks.super.getDataTypeByName(name);
			}
        };
        try {
        	FormulaEngine.setHooks(def);
	        FormulaRuntimeContext fc = new SystemFormulaContext(null);
	        try {
	        	ContextualFormulaFieldInfo fieldInfo = fc.lookup("OriginDateTime");
	        	FormulaFieldReference ffr = new FormulaFieldReferenceImpl(null, "OriginDateTime");
	        	def.getFieldReferenceValue(fieldInfo, fieldInfo.getDataType(), fc, ffr, false);
	        } catch (FormulaException ex) {
	        	
	        }
        } finally {
        	
        }
	}
	
}