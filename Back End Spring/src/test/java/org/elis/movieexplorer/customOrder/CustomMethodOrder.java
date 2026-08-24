package org.elis.movieexplorer.customOrder;

import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

public class CustomMethodOrder implements MethodOrderer{
    @Override
    public void orderMethods(MethodOrdererContext context) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        context.getMethodDescriptors().sort(
            switch (today){
                case MONDAY -> Comparator.comparingInt((MethodDescriptor desc) -> uppercaseSort(desc)).reversed();
                case TUESDAY -> Comparator.comparingInt((MethodDescriptor desc) -> jojoSort(desc)).reversed();
                case WEDNESDAY -> Comparator.comparingInt((MethodDescriptor desc) -> nameLengthSort(desc)).reversed();
                case THURSDAY -> Comparator.comparingInt(((MethodDescriptor desc) -> dndSort(desc))).reversed();
                case FRIDAY -> Comparator.comparingInt((MethodDescriptor desc) -> charPositionSort(desc)).reversed();
                case SATURDAY -> Comparator.comparingInt((MethodDescriptor desc) -> thermalSort(desc)).reversed();
                case SUNDAY -> Comparator.comparingInt((MethodDescriptor desc) -> scarabeoSort(desc)).reversed();
            }
        );
    }

    // Monday
    public int uppercaseSort(MethodDescriptor descriptor){
        String methodName = descriptor.getMethod().getName();
        return (int) methodName.chars().filter(c -> Character.isUpperCase(c)).count();
    }

    // Tuesday
    public int jojoSort(MethodDescriptor descriptor){
        Optional<Jojo> jojo = descriptor.findAnnotation(Jojo.class);
        String jojoCharacter;
        if(jojo.isPresent()){
            jojoCharacter = jojo.get().jojoChar();
        }else{
            jojoCharacter = "Jonathan";
        }

        return switch (jojoCharacter){
            case "Joseph" -> 2;
            case "Jotaro" -> 3;
            case "Josuke" -> 4;
            case "Giorno" -> 5;
            case "Jolyne" -> 6;
            case "Johnny" -> 7;
            default -> 1;
        };
    }

    // Wednesday
    public int nameLengthSort(MethodDescriptor descriptor){
        return descriptor.getMethod().getName().length();
    }

    // Thursday
    public int dndSort(MethodDescriptor descriptor){
        Optional<Dnd> dnd = descriptor.findAnnotation(Dnd.class);
        if(dnd.isPresent()){
            String classe = dnd.get().classe();
            return switch(classe){
                case "Barbaro" -> 1;
                case "Paladino" -> 2;
                case "Monaco" -> 3;
                case "Bardo" -> 4;
                case "Stregone" -> 5;
                case "Lottatore" -> 6;
                default -> 7;
            };
        }
        return 7;
    }

    // Friday
    public int charPositionSort(MethodDescriptor descriptor){
        char c = descriptor.getMethod().getName().charAt(0);
        return 'z' - c;
    }

    // Saturday
    public int thermalSort(MethodDescriptor descriptor){
        String name = descriptor.getMethod().getName();
        int calore =0;

        for(int i=0; i<name.length(); i++){
            char attuale = name.charAt(i);
            char prossimo = name.charAt(i+1);
            if(Character.isLowerCase(attuale)!=Character.isLowerCase(prossimo)){
                calore += 10;
            }
            if(Character.isDigit(prossimo)){
                calore += 20;
            }
        }
        return calore;
    }

    // Sunday
    public int scarabeoSort(MethodDescriptor descriptor){
        return descriptor.getMethod().getName().chars().map(
                c -> switch (c){
                    case 'q', 'z' -> 10;
                    case 'k' -> 5;
                    case 'j', 'x' -> 8;
                    default -> 1;
                }).sum();
    }
}
