package net.romvoid95.api.config.values;

import net.romvoid95.api.config.*;
import net.romvoid95.api.config.def.*;

public class OptArrayInteger extends OptValue {
	
	private int[] defaultValues;
	private RangePair<Integer> range;
    private boolean hasRange;

	public OptArrayInteger(ConfigKey key, ConfigCat category, ConfigComment comment, int... values) {
		super(Type.INTEGER_ARRAY, key, category, comment);
		this.defaultValues = values;
		this.hasRange = false;
	}
	
	public OptArrayInteger(ConfigKey key, ConfigCat category, ConfigComment comment, int minValueInt, int maxValueInt, int[] values) {
		this(key, category, comment, values);
		this.range = RangePair.of(minValueInt, maxValueInt);
		this.hasRange = true;
	}
	
    public boolean hasRange() {
    	return this.hasRange;
    }
    
    public int min() {
    	return this.range.min();
    }
    
    public int max() {
    	return this.range.max();
    }
	
	public int[] get() {
        return this.defaultValues;
    }

    public void set(int... values) {
        this.defaultValues = values;
    }
}
