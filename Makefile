

dev:
ifeq ($(OS),Windows_NT)
	gradlew.bat quarkusDev
else
	./gradlew quarkusDev
endif
