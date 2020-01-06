import { Component, OnInit, Input } from '@angular/core';
import { ControlContainer, NgForm } from '@angular/forms'
import { Request } from '../../model/Request';
import { DatabaseConfiguration } from '../../model/database/DatabaseConfiguration';
import { Database } from '../../model/database/Database';
import { EnvironmentVariable } from '../../model/container/EnvironmentVariable';
import { RandomWord } from '../../services/randomWord';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from '../../animations/animations';

@Component({
  selector: 'app-advanced-database',
  templateUrl: './advanced-database.component.html',
  styleUrls: ['./advanced-database.component.css'],
  viewProviders: [ {provide: ControlContainer, useExisting: NgForm} ],
  animations: [
    trigger(
      'enterAnimation', [
      transition(':enter', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '0',
            endHeight: '*',
            time: '250'
          }
        }
        )
      ]),
      transition(':leave', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '*',
            endHeight: '0',
            time: '250'
          }
        }
        )
      ])
    ]
    )
  ]
})
/**
 * Class which is necessary to render databases in advanced mode
 */
export class AdvancedDatabaseComponent implements OnInit {
  /** The request for getting the list of databases */
  @Input()
  request: Request;

  /** Wheter the form was already submitted or not */
  @Input()
  submitted: boolean;

  /** The list of all available databases of the backend */
  @Input()
  availableDatabases: DatabaseConfiguration[];

  constructor() { }

  ngOnInit() {
  }

  /**
   * Adds a new database to the request
   */
  addDatabase(): void {
    this.request.databases.push(new Database());
  }

  /**
   * Deletes a database from the request and deletes all corresponding links
   * 
   * @param database The database which should get deleted from the request
   */
  deleteDatabase(database: Database): void {
    this.deleteLinks(database);
    this.request.databases = this.request.databases.filter(d => d !== database);
  }

  /**
   * Deletes all links which start or end at the database
   * 
   * @param database The database which corresponding links should get deleted
   */
  deleteLinks(database: Database) {
    var links = [];

    for (let link of this.request.links) {
      if (link.containerFrom !== database.uniqueName && link.containerTo !== database.uniqueName) {
        links.push(link);
      }
    }

    this.request.links = links;
  }

  /**
   * Copys all environment variables from the database configuration in the actual databse
   * 
   * @param database The database for which the environment variables should get fetched
   */
  setEnvironmentsForDatabase(database: Database): void {
    database.environments = [];

    let databaseConfiguration = null;
    for (let i = 0; i < this.availableDatabases.length && databaseConfiguration == null; i++) {
      if (database.image === this.availableDatabases[i].name) {
        databaseConfiguration =  this.availableDatabases[i];
      }
    }

    if (databaseConfiguration !== null && databaseConfiguration.environmentVariables !== null) {
      for (let i = 0; i < databaseConfiguration.environmentVariables.length; i++) {
        let envVar = new EnvironmentVariable();
        envVar.name = databaseConfiguration.environmentVariables[i].name;
        envVar.value = RandomWord.getRandomWordCombination();
        envVar.secret = databaseConfiguration.environmentVariables[i].secret;

        database.environments.push(envVar);
      }
    }
  }
}
