<template>
  <div >
      <h1>Manejo de vistas y eventos dinámicos</h1>
   
  
  
      <div class="contenedorP ">
  
        <div  class="formulario">
  <div class="v-form" >
    <b-card v-show="showElement" draggable @dragstart="startDrag($event, form)">
        <template #header>PELICULA</template>
        <b-form  >
          <b-form-group label="Nombre:" label-for="input-1">
            <b-form-input id="input-1" v-model="form.nombre" placeholder="Title" required>
            </b-form-input>
          </b-form-group>
  
          <b-form-group label="Autor:" label-for="input-2">
            <b-form-input id="input-2" v-model="form.nombreAutor" placeholder="Name" required>
            </b-form-input>
          </b-form-group>
          <br>
          <b-form-group label="Genero:" label-for="input-3">
            <b-form-select v-model="form.genero" :options="options"></b-form-select>
          </b-form-group>
          

            <b-form-group label="Año:" label-for="input-4">
            <b-form-input id="input-1" v-model="form.fechaPublicacion" placeholder="Year" required>
            </b-form-input>
          </b-form-group>
  
  
         
        </b-form>
        </b-card>
  </div>
        
        </div>
        <!-- Grupo de cards -->
  
        <div
        @drop="onDrop($event)"
        @dragover.prevent
        @dragenter.prevent > 
        <transition-group name="bounceRight" tag="div" class="cards">
          <b-card v-for="(libro, index) in libros " :key="index">
  
            <template #header>{{ libro.nombre }}</template>
            <b-card-text>
              <p><strong>Autor:</strong> {{ libro.nombreAutor }}</p>
              <p><strong>Género:</strong> {{ libro.genero }}</p>
              <p><strong>Año de Publicación:</strong> {{ libro.fechaPublicacion }}</p>
            </b-card-text>
          </b-card>
      </transition-group>
      </div>
  
  
  <div class="d-flex justify-content-center align-items-center" >
      <b-spinner v-if="showSpinner" variant="danger" style="width: 8rem; height: 8rem; margin-top:-20%;"></b-spinner> 
    </div>
  
  
  
      </div>
  
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        options: [
          { value: null, text: "Selecciona un genero" },
          { value: "terror", text: "Terror" },
          { value: "novela", text: "Novela" },
          { value: "accion", text: "Acción"},
          { value: "cuento", text: "Cuento"},
          { value: "poesía", text: "Poesía"},
          { value: "humor", text: "Humor"},
          { value: "ficción", text: "Ficción"},
          { value: "libro de autoayuda", text: "Libro de autoayuda"},
          
        ],
       form:{},
       libros: [],
       showSpinner: false,
       showElement: true,
      lastScrollPosition: 0
      };
    },
    mounted() {
    this.spinner();
    window.addEventListener("scroll", this.onScroll); 
  },
  beforeDestroy() {
      window.removeEventListener("scroll", this.onScroll);
    },
    methods: {
 
    
  
      spinner() {
  this.showSpinner = true;
  setTimeout(() => {
    this.showSpinner = false;
    this.getLibros();
  }, 1500);
  },
  
  getLibros() {
    fetch("http://localhost:8080/api/libros/")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Success:", data);
        this.libros = data;
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  },
  
  
  startDrag(evt, item) {
        evt.dataTransfer.dropEffect = "move";
        evt.dataTransfer.effectAllowed = "move";
        evt.dataTransfer.setData("formData", JSON.stringify(item));
      },
    
  onDrop(evt) {
        const itemData = evt.dataTransfer.getData("formData");
        const item = JSON.parse(itemData);
        console.log("Datos drop" + itemData);
        this.libros.unshift(item);

  
        var url = "http://localhost:8080/api/libros/";
          fetch(url, {
            method: "POST", 
            body: JSON.stringify(item), 
            headers: {
              "Content-Type": "application/json",
            },
          })
          .then((res) => res.json())
          .then((response) => {
          console.log("Success:", response);
      })
      .catch((error) => console.error("Error:", error));
      },
    onScroll() {
  
        
        const currentScrollPosition = window.pageYOffset || document.documentElement.scrollTop;
      
     
  
        if (Math.abs(currentScrollPosition - this.lastScrollPosition) < 65) {
          return;
        }
  
       this.showElement = currentScrollPosition < this.lastScrollPosition;
        //  
        this.lastScrollPosition = currentScrollPosition;
    },
    
  },
  };
  </script>
  
  <style>
  
  .cards{
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  grid-gap: 20px; 
  margin-top: 20px;
  background-color: rgb(203, 224, 233);
  
  }
  
  .formulario{
 
  height: 120px;
  margin-bottom: 50px;
  
  
  }
  .contenedorP{
  margin-top: 40px;
  margin-right: 40px;
  margin-left: 40px;

  
  
  }

 .v-form{
  max-height:10px ;
 }

  
  </style>