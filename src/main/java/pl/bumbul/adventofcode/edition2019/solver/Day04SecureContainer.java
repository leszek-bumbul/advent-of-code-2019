package pl.bumbul.adventofcode.edition2019.solver;

import lombok.extern.log4j.Log4j2;
import pl.bumbul.adventofcode.edition2019.ResourceLoader;
import pl.bumbul.adventofcode.edition2019.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Log4j2
public class Day04SecureContainer implements Task {

    private Long[] resource;

    @Override
    public void execute() {
        initiateRange();
        log.info("--- Day 4: Secure Container ---");
        log.info("Stage 1 solution: {}", numberOfPasswords.get().count());
        log.info("Stage 2 solution: {}", numberOfPasswords.get().filter(filterOutPasswordsForSecondStage).count());//290
    }

    void initiateRange() {
        resource = ResourceLoader.loadFileWithOneEntryPerRow("Day04SecureContainer.input").toArray(Long[]::new);
    }

    Predicate<List<Integer>> increasingDigits = digits -> IntStream.range(0, digits.size() - 1)
            .noneMatch(i -> digits.get(i) > digits.get(i + 1));


    Predicate<List<Integer>> doubleFound = digits -> IntStream.range(0, digits.size() - 1)
            .anyMatch(i -> Objects.equals(digits.get(i), digits.get(i + 1)));

    LongPredicate verifyNumber = number -> {
        List<Integer> list = String.valueOf(number).chars().boxed().collect(Collectors.toList());
        return doubleFound.test(list) && increasingDigits.test(list);
    };

    Predicate<List<Integer>> onlyDouble = digits -> IntStream.range(0, digits.size() - 1)
            .anyMatch(i -> Objects.equals(digits.get(i), digits.get(i + 1))
                    && (i == digits.size() - 2 || !Objects.equals(digits.get(i + 1), digits.get(i + 2)))
                    && (i == 0 || !Objects.equals(digits.get(i - 1), digits.get(i))));

    LongPredicate filterOutPasswordsForSecondStage = number -> {
        List<Integer> list = String.valueOf(number).chars().boxed().collect(Collectors.toList());
        return onlyDouble.test(list);
    };


    private Supplier<LongStream> numberOfPasswords = () -> LongStream.rangeClosed(resource[0], resource[1]).filter(verifyNumber);

}
