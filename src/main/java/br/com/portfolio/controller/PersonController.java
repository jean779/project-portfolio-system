package br.com.portfolio.controller;

import br.com.portfolio.model.Person;
import br.com.portfolio.model.ProjectMember;
import br.com.portfolio.service.PersonService;
import br.com.portfolio.util.ViewPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/person")
    public String listPeople(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Model model) {
        Page<Person> peoplePage = personService.findPaginated(page, size);

        peoplePage.forEach(person -> {
            if (person.getProjectMemberships() != null) {
                for (ProjectMember member : person.getProjectMemberships()) {
                    member.getProject().getName();
                }
            }
        });

        model.addAttribute("peoplePage", peoplePage);
        return ViewPaths.PERSON_LIST;
    }

}
