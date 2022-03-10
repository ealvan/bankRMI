public class Transaction{};
// class Transaction{
//     private Object obj,obj2;
//     synchronized public void doIt(){
//         // ATOMICALLY {
//         //     System.out.println("Hello");
//         // }

//     }
// };

// class Cell{
//     private int value;
//     public void swapContents(Cell other){
//         synchronized(this){
//             synchronized(other){
//                 int newValue = other.getValue();
//                 other.setValue(value);
//                 value = newValue;        
//             }
//         }
//     }
//     public int getValue(){
//         return value;
//     }
//     public void setValue(int val){
//         value = val;
//     }


// }