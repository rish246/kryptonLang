#include <iostream>

int y = 5;

void forwardReference() {
	std::cout << y << std::endl;
}



int main() {
	y = 10;
	forwardReference();
}
