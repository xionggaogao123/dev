package com.pojo.exercise;

/**
 * 题目类型
 * @author fourer
 *
 */
public enum ExerciseItemType {
   
	SINGLECHOICE(1,"选择题","ch"),
    MULTICHOICE(2,"多选题","ch"),
	TRUE_OR_FALSE(3,"判断题","tf"),
	GAP(4,"填空题","gap"),
	SUBJECTIVE(5,"主观题","sub")
	
	;
	private int type;
	private String name;
	private String field;
	
	
	
	private ExerciseItemType(int type, String name,String field) {
		this.type = type;
		this.name = name;
		this.field=field;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public static ExerciseItemType getExerciseItemType(int type)
	{
		for(ExerciseItemType itemType:ExerciseItemType.values())
		{
			if(itemType.getType()==type)
				return itemType;
		}
		return null;
	}
}
