import { Component, OnInit, Input } from '@angular/core';
import { MainWizardOptions } from 'src/app/model/wizard/MainWizardOptions';
import { RequestService } from 'src/app/services/request.service';
import { DatabaseConfiguration } from 'src/app/model/database/DatabaseConfiguration';
import { Database } from 'src/app/model/database/Database';
import { EnvironmentVariable } from 'src/app/model/container/EnvironmentVariable';
import { RandomWord } from 'src/app/services/randomWord';
import { Request } from 'src/app/model/Request';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from 'src/app/animations/animations';

@Component({
  selector: 'app-wizard-database',
  templateUrl: './wizard-database.component.html',
  styleUrls: ['./wizard-database.component.css'],
  animations: [
    trigger(
      'fastEnterAnimation', [
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
    ),
    trigger(
      'enterAnimation', [
      transition(':enter', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '0',
            endHeight: '*',
            time: '500'
          }
        }
        )
      ]),
      transition(':leave', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '*',
            endHeight: '0',
            time: '500'
          }
        }
        )
      ])
    ]
    ),
    trigger(
      "blockInitialRenderAnimation",
      [
        transition(":enter", [])
      ]
    )
  ]
})
/** Class which is necessary for displaying the wizard database page */
export class WizardDatabaseComponent implements OnInit {
  /** Global wizard options */
  @Input()
  mainWizardOptions: MainWizardOptions;

  /** Request which gets created */
  @Input()
  request: Request;

  /** Database which currently is being modified */
  @Input()
  database: Database

  /** Available database configurations from the backend */
  databaseConfigurations: DatabaseConfiguration[];

  /** Submit Type for getting next page */
  submitType: string;

  /** If the form was already submitted or not */
  submitted: boolean = false;

  constructor(private requestService: RequestService) { }

  /**
   * Starts fetching all available databases from the backend
   * Initializes a new database if no database was given
   */
  ngOnInit() {
    this.getDatabaseConfiguration();

    if (this.database === null || this.database === undefined) {
      this.database = new Database();

      if (this.mainWizardOptions.addDatabase) {
        this.database.image = null;
      } else {
        this.database.image = 'NONE'
      }
    }
  }

  /**
   * Fetches all database configurations from the backend
   */
  getDatabaseConfiguration() {
    this.requestService.getAvailableDatabases().subscribe(c => this.databaseConfigurations = c,
      err => {
        console.log("Failed to fetch databases. Trying again in 5 Seconds");
        setTimeout(() => this.getDatabaseConfiguration(), 5000);
      });
  }

  /**
   * Creates the environment variables for the database
   * 
   * @param database The database for which the environment variables should get created
   */
  setEnvironmentsForDatabase(database: Database): void {
    database.environments = [];

    if (database.image === 'NONE') {
      return;
    }

    let databaseConfiguration = null;
    for (let i = 0; i < this.databaseConfigurations.length && databaseConfiguration == null; i++) {
      if (database.image === this.databaseConfigurations[i].name) {
        databaseConfiguration = this.databaseConfigurations[i];
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

  /**
   * Gets next page of the wizard
   */
  getNextPage() {
    if (this.submitType === 'CONTINUE') {
      if (this.database.image !== 'NONE') {
        this.request.databases.push(this.database);
      }
      this.mainWizardOptions.page = 'LINK';
    }

    if (this.submitType === 'EDIT') {
      this.mainWizardOptions.page = 'RESULT';
    }

    if (this.submitType === 'SKIP') {
      this.mainWizardOptions.page = 'LINK';
    }

    if (this.submitType === 'ADD') {
      if (this.database.image !== 'NONE') {
        this.request.databases.push(this.database);
      }
      this.reset();
    }

    if (this.submitType === 'ABORT_INSERT') {
      this.mainWizardOptions.page = 'RESULT';
    }

    if (this.submitType === 'INSERT') {
      this.request.databases.push(this.database);
      this.mainWizardOptions.page = 'RESULT';
    }
  }

  /**
   * Resets the database page
   */
  reset() {
    this.database = new Database();
    this.database.image = 'NONE';
  }

}
