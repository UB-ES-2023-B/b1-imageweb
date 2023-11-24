import { Component  } from '@angular/core';
import { GlobalDataService } from '../../services/global-data.service'
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  private usernameSubscription: Subscription = new Subscription();
  private profilePicSubscription: Subscription = new Subscription();

  username: string = '';
  profilePicUrl = this.globalDataService.getProfilePicture().previousUrl;

  isSearchBarOpen: boolean = false;
  resultsAvailable: boolean = false;
  loading: boolean = false;
  searchQuery: string = '';
  searchResults: any[] = [];

  constructor(private globalDataService:GlobalDataService,
              private router: Router,
              private userService: UserService,
              private toastr: ToastrService) {}

  ngOnInit(): void {
    this.usernameSubscription = this.globalDataService.username$.subscribe(username => {
      this.username = username;
    });
    this.profilePicSubscription = this.globalDataService.profilePicture$.subscribe(profilePhoto => {
      this.profilePicUrl = profilePhoto.previousUrl || '../assets/images/perfil.jpg';
    });
    this.getUserData();
  }

  ngOnDestroy() {
    this.usernameSubscription.unsubscribe();
    this.profilePicSubscription.unsubscribe();
  }

  private getUserData(): void {
    this.userService.getUser(this.globalDataService.getUsername()).subscribe(
      (response) => {
        if(response.body.profilePicture !== null) {
          this.profilePicUrl = `data:image/${response.body.profilePicture.photoName};base64,${response.body.profilePicture.data}`;
        }
      },
      (error) => {
        console.error('Error al obtener la foto de perfil', error);
      }
    );
  }

  goToProfile(itemActive:string): void {
    this.globalDataService.setActiveItem(itemActive);
    this.router.navigate(['/profile']);
  }

  logout() {
    this.globalDataService.clearSession();
    this.router.navigate(['/#']); // Redirige al usuario a la página de inicio
  }

  uploadImage(): void {
      console.log('Cargando imagen en proceso');
  }

  toggleSearchBar() {
    this.isSearchBarOpen = !this.isSearchBarOpen;
  }

  closeSearchBar(): void {
    this.isSearchBarOpen = false;
    this.searchQuery = '';
    this.resultsAvailable = false;
  }

  search(): void {
    if (this.searchQuery.trim() !== '') {
      this.loading = true;
      // Llama al servicio para obtener resultados de búsqueda
      this.userService.getSearchResults(this.searchQuery).subscribe(
        (response: any) => {
          // Manejar los resultados como sea necesario
          if (response.status === 200) {
            this.resultsAvailable = true;
            this.searchResults = response.body;
            this.deleteOwnUser(); // Borra al propio user
            console.log('Resultados de búsqueda:', response.body);
          }
          else this.toastr.error("Error en la búsqueda, inténtelo de nuevo más tarde.");
          this.loading = false;
        },
        (error) => {
          console.error('Error al realizar la búsqueda', error);
          this.toastr.error("Error en la búsqueda, inténtelo de nuevo más tarde.");
          this.loading = false;
        }
      );
    }
    else this.toastr.error("El campo de búsqueda no puede estar vacío. Inténtelo de nuevo.");
  }

  // Método para construir la URL de la foto de perfil del usuario
  getProfilePicUrl(user: any): string {
    if (user && user.profilePicture) {
      const photoName = user.profilePicture.photoName;
      const photoExtension = user.profilePicture.photoExtension;
      return `path/al/directorio/de/tu/api/${photoName}.${photoExtension}`;
      // Asegúrate de proporcionar la ruta correcta de tu API
    } else {
      // Devolver una URL predeterminada o una URL de imagen de marcador de posición
      return 'path/de/imagen/por/defecto.jpg';
    }
  }

  goToUserProfile(id: number) {
    console.log("ID USER", id);
    this.closeSearchBar();
    this.router.navigate([`/user-profile/${id}`]);
  }

  private deleteOwnUser() {
    this.searchResults = this.searchResults.filter(item => item.username != this.globalDataService.getUsername());
  }
}
