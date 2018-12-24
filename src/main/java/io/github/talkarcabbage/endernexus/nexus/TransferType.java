package io.github.talkarcabbage.endernexus.nexus;

public enum TransferType {
	NONE,
	ACCEPT,
	EXPORT,
	BOTH;
	
	public static TransferType fromInt(int type) {
		switch (type) {
		case 0:
			return NONE;
		case 1:
			return ACCEPT;
		case 2:
			return EXPORT;
		case 3:
			return BOTH;
		default:
			return NONE;
		}
	}
	
	public static int toInt(TransferType type) {
		switch (type) {
		case NONE:
			return 0;
		case ACCEPT:
			return 1;
		case EXPORT:
			return 2;
		case BOTH:
			return 3;
		default:
			return 0;
		}
	}
	
}
