package block.chain.market.users;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	private final UserModelAssembler assembler;

	UserController(UserModelAssembler assembler) {
		this.assembler = assembler;
	  }


	@GetMapping
	CollectionModel<EntityModel<User>> all() {

		List<EntityModel<User>> users = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

	  return new CollectionModel<>(users,
	    linkTo(methodOn(UserController.class).all()).withSelfRel());
	}

	@PostMapping("/createUser")
	ResponseEntity<?> createUser(@RequestBody User newUser){

	  EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));

	  return ResponseEntity
	    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    .body(entityModel);
	}
	
	@PostMapping("/login")
	ResponseEntity<?> checkExistingUser(@RequestBody User newUser){
		
		ExampleMatcher userDataMatcher = ExampleMatcher.matching()
				.withIgnorePaths("id")
				.withMatcher("username", GenericPropertyMatchers.exact())
				.withMatcher("password", GenericPropertyMatchers.exact());
		
		Example<User> authData = Example.of(newUser, userDataMatcher);

		if(repository.exists(authData)) {

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new VndErrors.VndError("Log In", "Successful !"));
		} else {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(new VndErrors.VndError("Log In", "Failed !"));
		}
	}

	  // Single item

	  @GetMapping("/{id}")
	  EntityModel<User> one(@PathVariable Long id) {

		  User user  = repository.findById(id)
				  .orElseThrow(() -> new UserNotFoundException(id));
		  
		  return assembler.toModel(user);
	  }

	  @PutMapping("/{id}")
	  ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long id){

	    User updateUser = repository.findById(id)
	      .map(user -> {
	        user.setUsername(newUser.getUsername());
	        user.setPassword(newUser.getPassword());
	        return repository.save(user);
	      })
	      .orElseGet(() -> {
	        newUser.setId(id);
	        return repository.save(newUser);
	      });
	    
	    EntityModel<User> entityModel = assembler.toModel(updateUser);
	    
	    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    		.body(entityModel);
	  }

	  @DeleteMapping("/{id}")
	  ResponseEntity<?> deleteUser(@PathVariable Long id) {
	    
		  repository.deleteById(id);
		  
		  return ResponseEntity.noContent().build();
	  }
}