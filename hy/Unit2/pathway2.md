
### var  val 차이
`val` (value)
 * 불변타입변수.
 *  초기 값 할당 이후 변경 불가능. 초기화 가능
 * 초기화 시 값을 할당하지 않으면 반드시 타입 지정
 * 변수의 참조가 가리키는 객체의 내부 값은 변경 가능

> 초기화 예시
```kotlin
fun main(args: Array<String>){
    val myArray = arrayListOf("java") 
    myArray.add("python") 				//참조가 가르키는 객체 내부 변경 가능
    println("myArray : "$myArray")  	// 출력 : myArray : [java, python] 
}
```
<br>

`var` (variable)
* 가변타입변수. 초기화 후 같은 타입으로 값 변경 가능
* 다른 타입으로 변경 시 형변환 필요
* 필요시에만 사용
<br><br><br>


### 클래스 / 상속
* Child / subclass : 다른 클래스 아래 있는 모든 클래스 
* Parent / superclass / base class : 하위 클래스가 1개 이상 있는 모든 클래스 
* Root / top-level class : 계층 구조의 최상위 클래스 
* Inheritance : 하위 클래스가 상위 클래스의 모든 속성, 메서드를 포함하거나 상속받는 경우
 						abstract 클래스 또는 open키워드로 표시된 클래스만 상속가능 
* abstract 추상클래스 : 완전히 구현되지 않아 인스턴스화할 수 없는 클래스

- kotlin.Any : kotlin의 모든 클래스의 공통 슈퍼클래스 
- `override` 키워드 : 서브 클래스에서 속성과 함수를 재정의

하위 클래스 호출 시 constructor(생성자)가 호출되어 객체 인스턴스를 초기화함. 
: constructor는 전달된 인수 등 클래스의 모든 정보에서 인스턴스를 빌드함. 클래스가 상위 클래스에서 속성과 함수를 상속받을 때 constructor는 상위 클래스의 constructor를 호출하여 객체 인스턴스 초기화를 완료함.
<br><br><br>

### `with` 문
동일한 객체 인스턴스에서 여러 호출을 실행하여 코드를 단순화함
```kotlin
println("Capacity : ${squareCabin.capacity}")
with(squareCabin) {
	    println("squareCabin")
}
```

<br><br><br>

### 소수점 이하 자릿수 변경
```kotlin
println("Floor area: %.2f".format(floorArea()))
```

<br><br><br>

--------------------------------------------------
<br>

### 주택 클래스 계층 구조
 
```kotlin
import kotlin.math.PI
import kotlin.math.sqrt 

fun main() {
	//val dwelling = Dwelling()
    val squareCabin = SquareCabin(6, 50.0)
    println("squareCabin")
    println("Capacity : ${squareCabin.capacity}")
    println("Material : ${squareCabin.buildingMaterial}")
    println("Has room? : ${squareCabin.hasRoom()}")
    println("Floor Area : ${squareCabin.floorArea()}\n")
    
    with(squareCabin) {
	    println("squareCabin")
        println("Capacity : ${capacity}")
        println("Material : ${buildingMaterial}")
    	println("Has room? : ${hasRoom()}")   
    	println("Floor Area : ${floorArea()}\n")
    }
    
    val roundHut = RoundHut(3, 10.0)
    with(roundHut) {
    	println("roundHut")
        println("Capacity : ${capacity}")
        println("Material : ${buildingMaterial}")
    	println("Has room? : ${hasRoom()}")   
        getRoom()
    	println("Has room? : ${hasRoom()}")   
        getRoom()
        println("Carpet Size : ${calculateMaxCarpetSize()}")
    	println("Floor Area : ${floorArea()}\n")
    }
    
 	val roundTower = RoundTower(4, 15.5)
//  	val roundTower = RoundTower(4,2)
    with(roundTower) {
    	println("roundTower")
    	println("Capacity: ${capacity}")
    	println("Material: ${buildingMaterial}")
    	println("Has room? ${hasRoom()}")
        println("Carpet Size : ${calculateMaxCarpetSize()}")
    	println("Floor Area : %.2f".format(floorArea()))
    	println("Floor Area : ${floorArea()}\n")
	}
}

abstract class Dwelling(private var residents: Int){
    abstract val buildingMaterial : String 
    abstract val capacity : Int
    
    fun hasRoom() : Boolean {
        return residents < capacity
    }
	
    abstract fun floorArea() : Double
	
    fun getRoom() {
        if (capacity > residents ){
            residents++
            println("You Got a ROOM!")
        }
        else {
            println("Sorry")
        }
    }
}

class SquareCabin(residents: Int, val length :Double ) : Dwelling(residents) {
    override val buildingMaterial = "wood"
    override val capacity = 6
    
    override fun floorArea() :Double {
        return length * length
    }
}

open class RoundHut(residents : Int, val radius : Double) : Dwelling(residents){
    override val buildingMaterial = "Straw"
    override val capacity = 4
    
    override fun floorArea() :Double {
        return PI * radius * radius
    }
    
    fun calculateMaxCarpetSize() : Double {
        val diameter = 2 * radius  //지름
    	return sqrt(diameter * diameter / 2)
    }   
}

class RoundTower(residents : Int, radius : Double, val floors : Int = 2) : RoundHut(residents, radius){
// class RoundTower(residents : Int, val floors : Int) : RoundHut(residents){
    override val buildingMaterial = "Stone"
    override val capacity = 4 * floors
    override fun floorArea() : Double{
//         return PI * radius * radius * floors
        return super.floorArea() * floors
    }
}
```




