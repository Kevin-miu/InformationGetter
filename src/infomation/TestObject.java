package infomation;

public class TestObject {

	private String name;
	private int age;
	private Friend firend;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Friend getFirends() {
		return firend;
	}

	public void setFirends(Friend firends) {
		this.firend = firends;
	}

	public class Friend {
		private String firendName;
		private int friendAge;

		public Friend(String friendName_p, int friendAge_p) {
			this.firendName = friendName_p;
			this.friendAge = friendAge_p;
		}
	}
}
